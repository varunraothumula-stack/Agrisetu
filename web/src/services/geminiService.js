const API_KEY = import.meta.env.VITE_GEMINI_API_KEY;

export async function analyzeCropAdvisoryAI({ crop, stage, problem }) {
  if (!API_KEY) {
    return `AI Advisory for ${crop} (${stage}):\n• Issue: ${problem}\n• Organic Action: Spray Neem Oil 10,000 PPM (3ml/L) and ensure balanced root-zone moisture.\n• Fertilizer / Care: Apply 19:19:19 water-soluble NPK @ 1kg/acre in 150L water and check for waterlogging.`;
  }

  try {
    const res = await fetch(
      `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=${API_KEY}`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          contents: [
            {
              parts: [
                {
                  text: `You are an Indian agronomist. Provide concise, practical bullet-point advice for an Indian farmer. Crop: ${crop}, Growth Stage: ${stage}, Problem: ${problem}. Include organic & safe chemical options.`,
                },
              ],
            },
          ],
        }),
      }
    );
    const data = await res.json();
    return data?.candidates?.[0]?.content?.parts?.[0]?.text || 'Unable to generate advice right now.';
  } catch {
    return `Fallback Advice for ${crop}: Inspect leaf undersides, avoid water stagnation, and apply balanced micronutrients.`;
  }
}

export async function scanLeafDiseaseWithGemini(base64Data, mimeType) {
  if (!API_KEY) return null;

  try {
    const res = await fetch(
      `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=${API_KEY}`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          contents: [
            {
              parts: [
                {
                  text: 'Analyze this crop leaf image. Respond strictly in JSON with keys: disease, confidence, organic, chemical, prevention.',
                },
                {
                  inlineData: {
                    mimeType: mimeType || 'image/jpeg',
                    data: base64Data,
                  },
                },
              ],
            },
          ],
          generationConfig: { responseMimeType: 'application/json' },
        }),
      }
    );
    const data = await res.json();
    const raw = data?.candidates?.[0]?.content?.parts?.[0]?.text;
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
}

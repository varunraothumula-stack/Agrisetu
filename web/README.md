# AgriSetu: Farm to Market (Web)

Responsive React + Vite + Tailwind CSS web application for Indian farmers, buyers, and agri-input sellers.

## Run Locally

```bash
cd web
npm install
npm run dev
```

## Deploy with GitHub

### Option 1: Vercel / Netlify (Fastest)
1. Push this repository to GitHub (using AI Studio's **Push to GitHub** menu or `git push`).
2. Import the repository in **Vercel** or **Netlify**.
3. Set the **Root Directory** to `web`.
4. Build Command: `npm run build` | Output Directory: `dist`.
5. *(Optional)* Add `VITE_GEMINI_API_KEY` in Environment Variables for live Gemini Vision & Crop Advisory AI.

### Option 2: GitHub Pages
A workflow file is included at `web/.github/workflows/deploy.yml` (copy `.github` to your repo root if deploying from `web/`) to publish `web/dist` automatically to GitHub Pages.

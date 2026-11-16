import React, { useState, useEffect } from 'react';
import {
  Sprout, Store, TrendingUp, CloudSun, ScanLine, ShoppingBag,
  MessageSquare, BarChart3, Briefcase, ShieldCheck, LogIn,
  ShoppingCart, Plus, Check, X, MapPin, Search, ArrowRight
} from 'lucide-react';
import {
  DISTRICTS, TRANSLATIONS, INITIAL_LISTINGS, INITIAL_REQUESTS,
  MANDI_PRICES, WEATHER_5DAY, INITIAL_ADVISORIES, SAMPLE_DISEASES,
  INITIAL_PRODUCTS, INITIAL_POSTS, DEMO_USERS
} from './data/mockData';
import { analyzeCropAdvisoryAI, scanLeafDiseaseWithGemini } from './services/geminiService';

function usePersistentState(key, defaultValue) {
  const [state, setState] = useState(() => {
    try {
      const saved = localStorage.getItem(key);
      return saved ? JSON.parse(saved) : defaultValue;
    } catch {
      return defaultValue;
    }
  });
  useEffect(() => {
    localStorage.setItem(key, JSON.stringify(state));
  }, [key, state]);
  return [state, setState];
}

export default function App() {
  const [route, setRoute] = useState(() => window.location.hash.replace('#/', '') || 'home');
  const [lang, setLang] = useState('en');
  const [role, setRole] = usePersistentState('agrisetu_role', 'Farmer');
  const [userName, setUserName] = usePersistentState('agrisetu_user', 'Rameshwar Patil');
  const [district, setDistrict] = usePersistentState('agrisetu_district', 'Nashik, MH');

  const [listings, setListings] = usePersistentState('agrisetu_listings', INITIAL_LISTINGS);
  const [requests, setRequests] = usePersistentState('agrisetu_requests', INITIAL_REQUESTS);
  const [advisories, setAdvisories] = usePersistentState('agrisetu_advisories', INITIAL_ADVISORIES);
  const [products, setProducts] = usePersistentState('agrisetu_products', INITIAL_PRODUCTS);
  const [posts, setPosts] = usePersistentState('agrisetu_posts', INITIAL_POSTS);
  const [cart, setCart] = usePersistentState('agrisetu_cart', []);
  const [savedCrops, setSavedCrops] = usePersistentState('agrisetu_saved', ['lst_1', 'lst_4']);
  const [users, setUsers] = usePersistentState('agrisetu_users', DEMO_USERS);

  const [cartOpen, setCartOpen] = useState(false);
  const [toast, setToast] = useState(null);

  const t = TRANSLATIONS[lang];

  const navigate = (r) => {
    setRoute(r);
    window.location.hash = `#/${r}`;
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  useEffect(() => {
    const onHash = () => setRoute(window.location.hash.replace('#/', '') || 'home');
    window.addEventListener('hashchange', onHash);
    return () => window.removeEventListener('hashchange', onHash);
  }, []);

  const notify = (msg) => {
    setToast(msg);
    setTimeout(() => setToast(null), 3200);
  };

  const addToCart = (product) => {
    if (!product.inStock) return;
    setCart((prev) => {
      const found = prev.find((i) => i.id === product.id);
      if (found) return prev.map((i) => (i.id === product.id ? { ...i, qty: i.qty + 1 } : i));
      return [...prev, { ...product, qty: 1 }];
    });
    notify(`${product.name} added to cart`);
  };

  const navItems = [
    { id: 'home', label: t.home, icon: Sprout },
    { id: 'marketplace', label: t.marketplace, icon: Store },
    { id: 'mandi', label: t.mandi, icon: TrendingUp },
    { id: 'advisory', label: t.advisory, icon: CloudSun },
    { id: 'scanner', label: t.scanner, icon: ScanLine },
    { id: 'store', label: t.store, icon: ShoppingBag },
    { id: 'community', label: t.community, icon: MessageSquare },
    { id: 'dashboard', label: t.dashboard, icon: BarChart3 },
    { id: 'business', label: t.business, icon: Briefcase },
    ...(role === 'Admin' ? [{ id: 'admin', label: t.admin, icon: ShieldCheck }] : []),
  ];

  return (
    <div className="min-h-screen flex flex-col bg-[#f7faf6] text-gray-900">
      <header className="bg-green-800 text-white sticky top-0 z-30 shadow-md">
        <div className="max-w-7xl mx-auto px-4 py-3 flex flex-wrap items-center justify-between gap-3">
          <button onClick={() => navigate('home')} className="flex items-center gap-2 text-left cursor-pointer">
            <div className="w-10 h-10 rounded-full bg-white text-green-800 flex items-center justify-center font-bold shadow">
              <Sprout className="w-6 h-6" />
            </div>
            <div>
              <div className="font-bold text-lg leading-tight flex items-center gap-2">
                {t.appTitle}
                <span className="text-[11px] bg-amber-300 text-green-950 px-2 py-0.5 rounded font-bold">{role}</span>
              </div>
              <div className="text-xs text-green-100">{t.subtitle}</div>
            </div>
          </button>

          <div className="flex items-center gap-2 flex-wrap">
            <select
              value={district}
              onChange={(e) => setDistrict(e.target.value)}
              className="bg-green-700 text-white text-xs sm:text-sm rounded-lg px-2.5 py-2 border border-green-600"
            >
              {DISTRICTS.map((d) => <option key={d} value={d}>{d}</option>)}
            </select>

            <button
              onClick={() => setLang(lang === 'en' ? 'hi' : 'en')}
              className="bg-white/15 hover:bg-white/25 px-3 py-2 rounded-lg text-xs sm:text-sm font-semibold cursor-pointer"
            >
              {lang === 'en' ? 'हिन्दी' : 'English'}
            </button>

            <button
              onClick={() => navigate('login')}
              className="bg-white/15 hover:bg-white/25 px-3 py-2 rounded-lg text-xs sm:text-sm font-semibold flex items-center gap-1 cursor-pointer"
            >
              <LogIn className="w-4 h-4" />
              <span className="hidden sm:inline">{t.login}</span>
            </button>

            <button
              onClick={() => setCartOpen(true)}
              className="relative bg-amber-500 hover:bg-amber-600 text-white px-3 py-2 rounded-lg font-semibold flex items-center gap-1.5 cursor-pointer"
            >
              <ShoppingCart className="w-4 h-4" />
              <span className="text-xs sm:text-sm">{cart.reduce((s, i) => s + i.qty, 0)}</span>
            </button>
          </div>
        </div>

        <nav className="bg-green-900/60 border-t border-green-700/60 overflow-x-auto">
          <div className="max-w-7xl mx-auto px-4 flex gap-1 py-1.5">
            {navItems.map((item) => {
              const Icon = item.icon;
              const active = route === item.id;
              return (
                <button
                  key={item.id}
                  onClick={() => navigate(item.id)}
                  className={`flex items-center gap-1.5 px-3 py-2 rounded-lg text-xs sm:text-sm font-medium whitespace-nowrap transition cursor-pointer ${
                    active ? 'bg-white text-green-900 font-bold shadow-sm' : 'text-green-100 hover:bg-green-700/60'
                  }`}
                >
                  <Icon className="w-4 h-4" />
                  {item.label}
                </button>
              );
            })}
          </div>
        </nav>
      </header>

      <main className="flex-1 max-w-7xl w-full mx-auto px-4 py-6">
        {route === 'home' && (
          <HomePage
            lang={lang} t={t} role={role} userName={userName} district={district}
            listings={listings} navigate={navigate}
          />
        )}
        {route === 'marketplace' && (
          <MarketplacePage
            lang={lang} role={role} district={district}
            listings={listings} setListings={setListings}
            requests={requests} setRequests={setRequests}
            savedCrops={savedCrops} setSavedCrops={setSavedCrops}
            notify={notify}
          />
        )}
        {route === 'mandi' && <MandiPage />}
        {route === 'advisory' && <AdvisoryPage district={district} advisories={advisories} />}
        {route === 'scanner' && <DiseaseScannerPage />}
        {route === 'store' && <StorePage products={products} addToCart={addToCart} />}
        {route === 'community' && <CommunityPage posts={posts} setPosts={setPosts} userName={userName} notify={notify} />}
        {route === 'dashboard' && (
          <DashboardPage
            role={role} setRole={setRole} listings={listings}
            requests={requests} savedCrops={savedCrops} navigate={navigate}
          />
        )}
        {route === 'business' && <BusinessModelPage />}
        {route === 'login' && (
          <LoginPage
            role={role} setRole={setRole} userName={userName}
            setUserName={setUserName} navigate={navigate} notify={notify}
          />
        )}
        {route === 'admin' && (
          <AdminPage
            users={users} setUsers={setUsers}
            listings={listings} setListings={setListings}
            advisories={advisories} setAdvisories={setAdvisories}
            products={products} setProducts={setProducts}
            notify={notify}
          />
        )}
      </main>

      {cartOpen && (
        <CartModal
          cart={cart} setCart={setCart}
          onClose={() => setCartOpen(false)}
          notify={notify}
        />
      )}

      {toast && (
        <div className="fixed bottom-5 right-5 z-50 bg-green-900 text-white px-5 py-3 rounded-xl shadow-xl font-medium text-sm">
          {toast}
        </div>
      )}

      <footer className="bg-white border-t border-green-100 py-5 text-center text-xs text-gray-500">
        AgriSetu: Farm to Market • Empowering Indian Farmers, Buyers & Agri-Input Sellers
      </footer>
    </div>
  );
}

function HomePage({ lang, t, role, userName, district, listings, navigate }) {
  const weather = WEATHER_5DAY[0];
  return (
    <div className="space-y-6">
      <section className="bg-gradient-to-r from-green-800 to-green-600 text-white rounded-2xl p-6 shadow-md flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
        <div>
          <span className="inline-block bg-white/20 text-xs font-semibold px-3 py-1 rounded-full mb-2">
            {role} • <MapPin className="inline w-3 h-3" /> {district}
          </span>
          <h1 className="text-2xl sm:text-3xl font-extrabold">
            {lang === 'hi' ? `नमस्ते, ${userName}!` : `Namaste, ${userName}!`}
          </h1>
          <p className="text-green-100 text-sm mt-1">
            {lang === 'hi'
              ? 'बिचौलियों के बिना सीधे खरीदारों को अपनी उपज बेचें और ताज़ा मंडी भाव देखें।'
              : 'Sell produce directly to verified buyers, track live mandi rates, and get AI crop care.'}
          </p>
        </div>
        <div className="bg-white/15 backdrop-blur rounded-xl p-4 min-w-[210px]">
          <div className="text-xs text-green-100">{t.weatherToday} ({district})</div>
          <div className="text-3xl font-extrabold mt-1">{weather.max}°C <span className="text-base font-normal">/ {weather.min}°C</span></div>
          <div className="text-xs mt-1 text-amber-200">{weather.cond} • Rain: {weather.rain}%</div>
        </div>
      </section>

      <section>
        <h2 className="text-lg font-bold mb-3">{t.quickActions}</h2>
        <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-3">
          {[
            { label: t.sellCrop, route: 'marketplace', icon: Store, color: 'bg-green-100 text-green-800' },
            { label: t.mandi, route: 'mandi', icon: TrendingUp, color: 'bg-blue-100 text-blue-800' },
            { label: t.scanner, route: 'scanner', icon: ScanLine, color: 'bg-purple-100 text-purple-800' },
            { label: t.advisory, route: 'advisory', icon: CloudSun, color: 'bg-amber-100 text-amber-800' },
            { label: t.store, route: 'store', icon: ShoppingBag, color: 'bg-teal-100 text-teal-800' },
            { label: t.community, route: 'community', icon: MessageSquare, color: 'bg-indigo-100 text-indigo-800' },
          ].map((a) => {
            const Icon = a.icon;
            return (
              <button
                key={a.route}
                onClick={() => navigate(a.route)}
                className="bg-white p-4 rounded-xl border border-green-100 shadow-sm hover:shadow-md transition flex flex-col items-center gap-2 cursor-pointer"
              >
                <div className={`w-12 h-12 rounded-full flex items-center justify-center ${a.color}`}>
                  <Icon className="w-6 h-6" />
                </div>
                <span className="font-bold text-sm text-center">{a.label}</span>
              </button>
            );
          })}
        </div>
      </section>

      <section>
        <div className="flex justify-between items-center mb-3">
          <h2 className="text-lg font-bold">{t.topPrices}</h2>
          <button onClick={() => navigate('mandi')} className="text-green-700 font-semibold text-sm flex items-center gap-1 cursor-pointer">
            View All <ArrowRight className="w-4 h-4" />
          </button>
        </div>
        <div className="grid grid-cols-2 sm:grid-cols-4 lg:grid-cols-7 gap-3">
          {MANDI_PRICES.map((m) => (
            <div key={m.id} onClick={() => navigate('mandi')} className="bg-white p-3.5 rounded-xl border border-green-100 shadow-sm cursor-pointer hover:border-green-400">
              <div className="font-bold text-sm">{m.crop}</div>
              <div className="text-xl font-extrabold text-green-700 mt-1">₹{m.modal}</div>
              <div className="text-[11px] text-gray-500">/ Quintal</div>
              <div className="text-[11px] text-gray-600 mt-1 truncate">{m.mandi}</div>
            </div>
          ))}
        </div>
      </section>

      <section>
        <div className="flex justify-between items-center mb-3">
          <h2 className="text-lg font-bold">Fresh Produce Lots</h2>
          <button onClick={() => navigate('marketplace')} className="text-green-700 font-semibold text-sm flex items-center gap-1 cursor-pointer">
            Open Marketplace <ArrowRight className="w-4 h-4" />
          </button>
        </div>
        <div className="grid md:grid-cols-3 gap-4">
          {listings.slice(0, 3).map((l) => (
            <div key={l.id} className="bg-white rounded-2xl border border-green-100 overflow-hidden shadow-sm">
              <img src={l.photo} alt={l.crop} className="w-full h-40 object-cover" />
              <div className="p-4">
                <div className="flex justify-between items-start">
                  <div>
                    <h3 className="font-bold text-lg">{lang === 'hi' ? l.cropHi : l.crop}</h3>
                    <p className="text-xs text-gray-500">{l.variety} • {l.district}</p>
                  </div>
                  <span className="font-extrabold text-green-700 text-lg">₹{l.price}/Qtl</span>
                </div>
                <div className="mt-3 flex justify-between items-center text-xs text-gray-600">
                  <span>Qty: <b>{l.qty} Qtl</b></span>
                  <span>👤 {l.farmer} (★ {l.rating})</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}

function MarketplacePage({ lang, role, district, listings, setListings, requests, setRequests, savedCrops, setSavedCrops, notify }) {
  const [tab, setTab] = useState('browse');
  const [search, setSearch] = useState('');
  const [cropFilter, setCropFilter] = useState('All');
  const [organicFilter, setOrganicFilter] = useState('All');
  const [maxPrice, setMaxPrice] = useState(10000);
  const [showCreate, setShowCreate] = useState(false);
  const [offerListing, setOfferListing] = useState(null);

  const [form, setForm] = useState({
    crop: 'Wheat', variety: '', qty: '', price: '', harvestDate: '',
    organic: false, village: '', district, contact: '', photo: '',
  });
  const [formError, setFormError] = useState('');
  const [offerForm, setOfferForm] = useState({ buyer: '', qty: '', offeredPrice: '', pickupDate: '', message: '' });

  const filtered = listings.filter((l) => {
    const matchCrop = cropFilter === 'All' || l.crop === cropFilter;
    const matchOrg = organicFilter === 'All' || (organicFilter === 'Organic' ? l.organic : !l.organic);
    const matchPrice = l.price <= maxPrice;
    const matchSearch =
      l.crop.toLowerCase().includes(search.toLowerCase()) ||
      l.variety.toLowerCase().includes(search.toLowerCase()) ||
      l.district.toLowerCase().includes(search.toLowerCase());
    return matchCrop && matchOrg && matchPrice && matchSearch;
  });

  const handleCreateSubmit = (e) => {
    e.preventDefault();
    if (!form.variety.trim() || Number(form.qty) <= 0 || Number(form.price) <= 0 || !form.harvestDate || !form.village.trim() || !/^\+?\d[\d\s-]{8,}$/.test(form.contact)) {
      setFormError('Please fill all fields with valid quantity, price, harvest date, and phone number.');
      return;
    }
    const newItem = {
      id: `lst_${Date.now()}`,
      crop: form.crop,
      cropHi: form.crop,
      variety: form.variety,
      qty: Number(form.qty),
      price: Number(form.price),
      harvestDate: form.harvestDate,
      organic: form.organic,
      village: form.village,
      district: form.district,
      farmer: 'You (Farmer)',
      contact: form.contact,
      rating: 4.9,
      status: 'Active',
      photo: form.photo || 'https://images.unsplash.com/photo-1500382017468-9049fed747ef?auto=format&fit=crop&w=600&q=80',
    };
    setListings([newItem, ...listings]);
    setShowCreate(false);
    setFormError('');
    notify('Produce listing published!');
  };

  const handleSendOffer = (e) => {
    e.preventDefault();
    if (!offerForm.buyer.trim() || Number(offerForm.qty) <= 0 || Number(offerForm.offeredPrice) <= 0 || !offerForm.pickupDate) {
      notify('Enter valid buyer name, quantity, price & pickup date');
      return;
    }
    const newReq = {
      id: `req_${Date.now()}`,
      listingId: offerListing.id,
      crop: offerListing.crop,
      buyer: offerForm.buyer,
      qty: Number(offerForm.qty),
      offeredPrice: Number(offerForm.offeredPrice),
      pickupDate: offerForm.pickupDate,
      message: offerForm.message || 'Interested in direct pickup.',
      status: 'Pending',
    };
    setRequests([newReq, ...requests]);
    setListings(listings.map((l) => (l.id === offerListing.id ? { ...l, status: 'Negotiation' } : l)));
    setOfferListing(null);
    notify('Buy Request sent to farmer!');
  };

  const updateReqStatus = (reqId, newStatus) => {
    setRequests(requests.map((r) => (r.id === reqId ? { ...r, status: newStatus } : r)));
    if (newStatus === 'Accepted') {
      const req = requests.find((r) => r.id === reqId);
      if (req) setListings(listings.map((l) => (l.id === req.listingId ? { ...l, status: 'Sold' } : l)));
    }
    notify(`Request marked as ${newStatus}`);
  };

  const statusColor = (s) => ({
    Active: 'bg-green-100 text-green-800',
    Negotiation: 'bg-amber-100 text-amber-800',
    Sold: 'bg-gray-200 text-gray-800',
    Expired: 'bg-red-100 text-red-800',
  }[s] || 'bg-gray-100');

  return (
    <div className="space-y-5">
      <div className="flex flex-wrap justify-between items-center gap-3">
        <div className="flex gap-2">
          <button onClick={() => setTab('browse')} className={`px-4 py-2.5 rounded-xl font-bold text-sm cursor-pointer ${tab === 'browse' ? 'bg-green-700 text-white' : 'bg-white border border-green-200'}`}>
            Produce Lots ({listings.length})
          </button>
          <button onClick={() => setTab('requests')} className={`px-4 py-2.5 rounded-xl font-bold text-sm cursor-pointer ${tab === 'requests' ? 'bg-green-700 text-white' : 'bg-white border border-green-200'}`}>
            Buy Requests ({requests.length})
          </button>
        </div>

        <button onClick={() => setShowCreate(true)} className="bg-green-700 hover:bg-green-800 text-white px-4 py-2.5 rounded-xl font-bold text-sm flex items-center gap-1.5 cursor-pointer">
          <Plus className="w-4 h-4" /> List Crop for Sale
        </button>
      </div>

      {tab === 'browse' ? (
        <>
          <div className="bg-white p-4 rounded-2xl border border-green-100 grid sm:grid-cols-2 lg:grid-cols-4 gap-3">
            <div className="relative">
              <Search className="w-4 h-4 absolute left-3 top-3 text-gray-400" />
              <input value={search} onChange={(e) => setSearch(e.target.value)} placeholder="Search crop, variety, district..." className="w-full pl-9 pr-3 py-2 border border-gray-200 rounded-lg text-sm" />
            </div>
            <select value={cropFilter} onChange={(e) => setCropFilter(e.target.value)} className="border border-gray-200 rounded-lg px-3 py-2 text-sm">
              {['All', 'Wheat', 'Rice', 'Tomato', 'Onion', 'Potato', 'Cotton', 'Maize'].map((c) => (
                <option key={c} value={c}>{c === 'All' ? 'All Crops' : c}</option>
              ))}
            </select>
            <select value={organicFilter} onChange={(e) => setOrganicFilter(e.target.value)} className="border border-gray-200 rounded-lg px-3 py-2 text-sm">
              <option value="All">All Farming Types</option>
              <option value="Organic">Organic Only</option>
              <option value="Conventional">Conventional Only</option>
            </select>
            <div className="flex items-center gap-2 text-xs">
              <span>Max ₹{maxPrice}</span>
              <input type="range" min="1000" max="10000" step="250" value={maxPrice} onChange={(e) => setMaxPrice(Number(e.target.value))} className="flex-1 accent-green-700" />
            </div>
          </div>

          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
            {filtered.map((l) => {
              const isSaved = savedCrops.includes(l.id);
              return (
                <div key={l.id} className="bg-white rounded-2xl border border-green-100 overflow-hidden shadow-sm flex flex-col">
                  <img src={l.photo} alt={l.crop} className="w-full h-44 object-cover" />
                  <div className="p-4 flex-1 flex flex-col justify-between">
                    <div>
                      <div className="flex justify-between items-center mb-2">
                        <span className={`text-xs font-bold px-2.5 py-0.5 rounded-full ${statusColor(l.status)}`}>{l.status}</span>
                        {l.organic && <span className="text-xs font-bold bg-emerald-100 text-emerald-800 px-2 py-0.5 rounded">🌱 Organic</span>}
                      </div>
                      <div className="flex justify-between items-start">
                        <div>
                          <h3 className="font-bold text-lg">{lang === 'hi' ? l.cropHi : l.crop}</h3>
                          <p className="text-xs text-gray-500">{l.variety}</p>
                        </div>
                        <div className="text-right">
                          <div className="text-xl font-extrabold text-green-700">₹{l.price}</div>
                          <div className="text-[11px] text-gray-500">per Quintal</div>
                        </div>
                      </div>
                      <div className="mt-3 text-xs text-gray-600 space-y-1">
                        <div>📦 Quantity: <b>{l.qty} Quintals</b> • 📅 {l.harvestDate}</div>
                        <div>📍 {l.village}, {l.district}</div>
                        <div>👨‍🌾 {l.farmer} (★ {l.rating}) • 📞 {l.contact}</div>
                      </div>
                    </div>

                    <div className="mt-4 pt-3 border-t border-gray-100 flex gap-2">
                      <button
                        onClick={() => setSavedCrops(isSaved ? savedCrops.filter((id) => id !== l.id) : [...savedCrops, l.id])}
                        className={`px-3 py-2.5 rounded-xl border text-xs font-bold cursor-pointer ${isSaved ? 'bg-amber-50 border-amber-300 text-amber-700' : 'border-gray-200 text-gray-600'}`}
                      >
                        {isSaved ? '★ Saved' : '☆ Save'}
                      </button>
                      <button
                        disabled={l.status === 'Sold' || l.status === 'Expired'}
                        onClick={() => {
                          setOfferListing(l);
                          setOfferForm({ buyer: role === 'Buyer' ? 'FreshDirect Retail' : '', qty: l.qty, offeredPrice: l.price, pickupDate: '', message: '' });
                        }}
                        className="flex-1 bg-green-700 hover:bg-green-800 disabled:bg-gray-300 text-white py-2.5 rounded-xl font-bold text-sm cursor-pointer"
                      >
                        Send Buy Request
                      </button>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        </>
      ) : (
        <div className="space-y-3">
          {requests.map((r) => (
            <div key={r.id} className="bg-white p-4 rounded-2xl border border-green-100 shadow-sm flex flex-col sm:flex-row justify-between gap-4">
              <div>
                <div className="flex items-center gap-2">
                  <span className="font-bold text-base">{r.crop} — {r.buyer}</span>
                  <span className="text-xs px-2 py-0.5 rounded bg-green-100 text-green-800 font-bold">{r.status}</span>
                </div>
                <p className="text-sm text-gray-700 mt-1">
                  Qty: <b>{r.qty} Qtl</b> • Offer: <b>₹{r.offeredPrice}/Qtl</b> • Pickup: {r.pickupDate}
                </p>
                <p className="text-xs text-gray-500 mt-1">"{r.message}"</p>
              </div>
              {r.status === 'Pending' || r.status === 'Negotiating' ? (
                <div className="flex items-center gap-2">
                  <button onClick={() => updateReqStatus(r.id, 'Accepted')} className="bg-green-700 text-white px-3 py-2 rounded-lg text-xs font-bold cursor-pointer">Accept</button>
                  <button onClick={() => updateReqStatus(r.id, 'Negotiating')} className="bg-amber-500 text-white px-3 py-2 rounded-lg text-xs font-bold cursor-pointer">Negotiate</button>
                  <button onClick={() => updateReqStatus(r.id, 'Rejected')} className="bg-red-600 text-white px-3 py-2 rounded-lg text-xs font-bold cursor-pointer">Reject</button>
                </div>
              ) : null}
            </div>
          ))}
        </div>
      )}

      {showCreate && (
        <div className="fixed inset-0 z-50 bg-black/50 flex items-center justify-center p-4">
          <form onSubmit={handleCreateSubmit} className="bg-white rounded-2xl max-w-md w-full p-5 space-y-3 max-h-[90vh] overflow-y-auto">
            <div className="flex justify-between items-center">
              <h3 className="font-bold text-lg">Create Produce Listing</h3>
              <button type="button" onClick={() => setShowCreate(false)}><X /></button>
            </div>
            {formError && <div className="text-xs text-red-600 bg-red-50 p-2 rounded">{formError}</div>}
            <select value={form.crop} onChange={(e) => setForm({ ...form, crop: e.target.value })} className="w-full border p-2.5 rounded-lg text-sm">
              {['Wheat', 'Rice', 'Tomato', 'Onion', 'Potato', 'Cotton', 'Maize'].map((c) => <option key={c}>{c}</option>)}
            </select>
            <input placeholder="Variety (e.g. Sharbati)" value={form.variety} onChange={(e) => setForm({ ...form, variety: e.target.value })} className="w-full border p-2.5 rounded-lg text-sm" />
            <div className="grid grid-cols-2 gap-2">
              <input type="number" placeholder="Quantity (Quintals)" value={form.qty} onChange={(e) => setForm({ ...form, qty: e.target.value })} className="border p-2.5 rounded-lg text-sm" />
              <input type="number" placeholder="Price ₹/Quintal" value={form.price} onChange={(e) => setForm({ ...form, price: e.target.value })} className="border p-2.5 rounded-lg text-sm" />
            </div>
            <input type="date" value={form.harvestDate} onChange={(e) => setForm({ ...form, harvestDate: e.target.value })} className="w-full border p-2.5 rounded-lg text-sm" />
            <div className="grid grid-cols-2 gap-2">
              <input placeholder="Village" value={form.village} onChange={(e) => setForm({ ...form, village: e.target.value })} className="border p-2.5 rounded-lg text-sm" />
              <select value={form.district} onChange={(e) => setForm({ ...form, district: e.target.value })} className="border p-2.5 rounded-lg text-sm">
                {DISTRICTS.map((d) => <option key={d}>{d}</option>)}
              </select>
            </div>
            <input placeholder="Contact Phone (+91...)" value={form.contact} onChange={(e) => setForm({ ...form, contact: e.target.value })} className="w-full border p-2.5 rounded-lg text-sm" />
            <label className="flex items-center gap-2 text-sm">
              <input type="checkbox" checked={form.organic} onChange={(e) => setForm({ ...form, organic: e.target.checked })} />
              Certified Organic Produce
            </label>
            <button type="submit" className="w-full bg-green-700 text-white py-3 rounded-xl font-bold cursor-pointer">Publish Crop Listing</button>
          </form>
        </div>
      )}

      {offerListing && (
        <div className="fixed inset-0 z-50 bg-black/50 flex items-center justify-center p-4">
          <form onSubmit={handleSendOffer} className="bg-white rounded-2xl max-w-md w-full p-5 space-y-3">
            <div className="flex justify-between items-center">
              <h3 className="font-bold text-lg">Buy Request: {offerListing.crop}</h3>
              <button type="button" onClick={() => setOfferListing(null)}><X /></button>
            </div>
            <input placeholder="Buyer / Company Name" value={offerForm.buyer} onChange={(e) => setOfferForm({ ...offerForm, buyer: e.target.value })} className="w-full border p-2.5 rounded-lg text-sm" />
            <div className="grid grid-cols-2 gap-2">
              <input type="number" placeholder="Qty (Qtl)" value={offerForm.qty} onChange={(e) => setOfferForm({ ...offerForm, qty: e.target.value })} className="border p-2.5 rounded-lg text-sm" />
              <input type="number" placeholder="Offered ₹/Qtl" value={offerForm.offeredPrice} onChange={(e) => setOfferForm({ ...offerForm, offeredPrice: e.target.value })} className="border p-2.5 rounded-lg text-sm" />
            </div>
            <input type="date" value={offerForm.pickupDate} onChange={(e) => setOfferForm({ ...offerForm, pickupDate: e.target.value })} className="w-full border p-2.5 rounded-lg text-sm" />
            <textarea placeholder="Pickup & payment notes..." value={offerForm.message} onChange={(e) => setOfferForm({ ...offerForm, message: e.target.value })} className="w-full border p-2.5 rounded-lg text-sm" rows={3} />
            <button type="submit" className="w-full bg-green-700 text-white py-3 rounded-xl font-bold cursor-pointer">Send Purchase Request</button>
          </form>
        </div>
      )}
    </div>
  );
}

function MandiPage() {
  const [cropFilter, setCropFilter] = useState('All');
  const [distFilter, setDistFilter] = useState('All');
  const [selected, setSelected] = useState(MANDI_PRICES[0]);

  const list = MANDI_PRICES.filter(
    (m) => (cropFilter === 'All' || m.crop === cropFilter) && (distFilter === 'All' || m.district === distFilter)
  );

  const minT = Math.min(...selected.trend) - 40;
  const maxT = Math.max(...selected.trend) + 40;
  const points = selected.trend
    .map((val, idx) => {
      const x = (idx / (selected.trend.length - 1)) * 560 + 20;
      const y = 140 - ((val - minT) / (maxT - minT)) * 110;
      return `${x},${y}`;
    })
    .join(' ');

  return (
    <div className="space-y-6">
      <div className="bg-white p-5 rounded-2xl border border-green-100 shadow-sm">
        <div className="flex flex-wrap justify-between items-center gap-2 mb-4">
          <div>
            <h2 className="text-xl font-bold">7-Day Price Trend: {selected.crop}</h2>
            <p className="text-xs text-gray-500">{selected.mandi} ({selected.district})</p>
          </div>
          <div className="text-2xl font-extrabold text-green-700">₹{selected.modal}/Qtl</div>
        </div>

        <svg viewBox="0 0 600 160" className="w-full h-44 bg-green-50/50 rounded-xl">
          <polyline fill="none" stroke="#15803d" strokeWidth="4" points={points} />
          {selected.trend.map((val, idx) => {
            const x = (idx / (selected.trend.length - 1)) * 560 + 20;
            const y = 140 - ((val - minT) / (maxT - minT)) * 110;
            return (
              <g key={idx}>
                <circle cx={x} cy={y} r="5" fill="#15803d" />
                <text x={x} y={y - 10} textAnchor="middle" fontSize="10" fill="#166534">₹{val}</text>
              </g>
            );
          })}
        </svg>
      </div>

      <div className="flex flex-wrap gap-3">
        <select value={cropFilter} onChange={(e) => setCropFilter(e.target.value)} className="bg-white border border-green-200 rounded-xl px-4 py-2 text-sm">
          <option value="All">All Crops</option>
          {['Wheat', 'Onion', 'Tomato', 'Rice', 'Cotton', 'Potato', 'Maize'].map((c) => <option key={c}>{c}</option>)}
        </select>
        <select value={distFilter} onChange={(e) => setDistFilter(e.target.value)} className="bg-white border border-green-200 rounded-xl px-4 py-2 text-sm">
          <option value="All">All Districts</option>
          {DISTRICTS.map((d) => <option key={d}>{d}</option>)}
        </select>
      </div>

      <div className="bg-white rounded-2xl border border-green-100 overflow-x-auto shadow-sm">
        <table className="w-full text-left text-sm">
          <thead className="bg-green-50 text-green-900">
            <tr>
              <th className="p-3.5">Crop</th>
              <th className="p-3.5">Mandi</th>
              <th className="p-3.5">Min (₹)</th>
              <th className="p-3.5">Max (₹)</th>
              <th className="p-3.5">Modal (₹)</th>
              <th className="p-3.5">Date</th>
            </tr>
          </thead>
          <tbody>
            {list.map((m) => (
              <tr
                key={m.id}
                onClick={() => setSelected(m)}
                className={`border-t border-gray-100 cursor-pointer hover:bg-green-50/40 ${selected.id === m.id ? 'bg-green-50' : ''}`}
              >
                <td className="p-3.5 font-bold">{m.crop}</td>
                <td className="p-3.5">{m.mandi}</td>
                <td className="p-3.5">₹{m.min}</td>
                <td className="p-3.5">₹{m.max}</td>
                <td className="p-3.5 font-extrabold text-green-700">₹{m.modal}</td>
                <td className="p-3.5 text-gray-500">{m.date}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

function AdvisoryPage({ district, advisories }) {
  const [crop, setCrop] = useState('Wheat');
  const [stage, setStage] = useState('Vegetative Growth');
  const [problem, setProblem] = useState('Yellowing leaves');
  const [aiResult, setAiResult] = useState('');
  const [loading, setLoading] = useState(false);

  const askAI = async (e) => {
    e.preventDefault();
    setLoading(true);
    const res = await analyzeCropAdvisoryAI({ crop, stage, problem });
    setAiResult(res);
    setLoading(false);
  };

  return (
    <div className="space-y-6">
      <section>
        <h2 className="text-xl font-bold mb-3">5-Day Weather Forecast ({district})</h2>
        <div className="grid grid-cols-2 sm:grid-cols-5 gap-3">
          {WEATHER_5DAY.map((w) => (
            <div key={w.day} className="bg-white p-4 rounded-2xl border border-green-100 shadow-sm">
              <div className="font-bold">{w.day} <span className="text-xs text-gray-400">{w.date}</span></div>
              <div className="text-2xl font-extrabold text-green-800 my-2">{w.max}° / {w.min}°C</div>
              <div className="text-xs font-semibold text-blue-700">{w.cond} ({w.rain}% rain)</div>
              <p className="text-xs text-gray-600 mt-2">{w.note}</p>
            </div>
          ))}
        </div>
      </section>

      <section className="grid lg:grid-cols-2 gap-6">
        <div className="space-y-3">
          <h2 className="text-xl font-bold">Crop-Specific Advisories</h2>
          {advisories.map((a) => (
            <div key={a.id} className="bg-white p-4 rounded-2xl border border-green-100 shadow-sm">
              <span className="text-xs font-bold bg-green-100 text-green-800 px-2 py-0.5 rounded">{a.crop} • {a.type}</span>
              <h3 className="font-bold mt-1">{a.title}</h3>
              <p className="text-sm text-gray-600 mt-1">{a.advice}</p>
            </div>
          ))}
        </div>

        <form onSubmit={askAI} className="bg-white p-5 rounded-2xl border border-green-200 shadow-sm space-y-3 h-fit">
          <h2 className="text-xl font-bold text-green-800">AI Crop Advice Assistant</h2>
          <select value={crop} onChange={(e) => setCrop(e.target.value)} className="w-full border p-2.5 rounded-xl text-sm">
            {['Wheat', 'Rice', 'Tomato', 'Onion', 'Potato', 'Cotton', 'Maize'].map((c) => <option key={c}>{c}</option>)}
          </select>
          <select value={stage} onChange={(e) => setStage(e.target.value)} className="w-full border p-2.5 rounded-xl text-sm">
            {['Sowing / Germination', 'Vegetative Growth', 'Flowering', 'Fruiting / Grain Fill', 'Harvesting'].map((s) => <option key={s}>{s}</option>)}
          </select>
          <input value={problem} onChange={(e) => setProblem(e.target.value)} placeholder="Describe issue..." className="w-full border p-2.5 rounded-xl text-sm" />
          <button type="submit" disabled={loading} className="w-full bg-green-700 hover:bg-green-800 text-white py-3 rounded-xl font-bold cursor-pointer">
            {loading ? 'Consulting AI Agronomist...' : 'Get AI Crop Advice'}
          </button>
          {aiResult && <pre className="whitespace-pre-wrap text-sm bg-green-50 p-4 rounded-xl text-green-950 border border-green-200">{aiResult}</pre>}
        </form>
      </section>
    </div>
  );
}

function DiseaseScannerPage() {
  const [selected, setSelected] = useState(SAMPLE_DISEASES[0]);
  const [preview, setPreview] = useState(null);
  const [scanning, setScanning] = useState(false);

  const handleFile = (e) => {
    const file = e.target.files?.[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = async () => {
      setPreview(reader.result);
      setScanning(true);
      const base64 = reader.result.split(',')[1];
      const aiRes = await scanLeafDiseaseWithGemini(base64, file.type);
      if (aiRes) {
        setSelected({ id: 'custom', crop: 'Uploaded Leaf', ...aiRes });
      } else {
        setSelected(SAMPLE_DISEASES[Math.floor(Math.random() * SAMPLE_DISEASES.length)]);
      }
      setScanning(false);
    };
    reader.readAsDataURL(file);
  };

  return (
    <div className="grid lg:grid-cols-2 gap-6">
      <div className="bg-white p-6 rounded-2xl border border-green-100 shadow-sm space-y-4">
        <h2 className="text-xl font-bold">Gemini AI Leaf Disease Scanner</h2>
        <p className="text-sm text-gray-600">Capture or upload a clear photo of an affected crop leaf, or choose a sample case below.</p>
        <label className="block border-2 border-dashed border-green-300 rounded-2xl p-8 text-center cursor-pointer hover:bg-green-50/50">
          {preview ? <img src={preview} alt="Leaf preview" className="max-h-56 mx-auto rounded-xl" /> : <div className="text-green-800 font-semibold">📷 Tap to Upload or Capture Leaf Photo</div>}
          <input type="file" accept="image/*" capture="environment" onChange={handleFile} className="hidden" />
        </label>
        <div className="flex flex-wrap gap-2">
          {SAMPLE_DISEASES.map((d) => (
            <button key={d.id} onClick={() => setSelected(d)} className={`px-3 py-1.5 rounded-lg text-xs font-bold cursor-pointer ${selected.id === d.id ? 'bg-green-700 text-white' : 'bg-green-50 text-green-800'}`}>
              Sample: {d.crop}
            </button>
          ))}
        </div>
      </div>

      <div className="bg-white p-6 rounded-2xl border border-green-100 shadow-sm space-y-4">
        {scanning ? (
          <div className="py-16 text-center font-bold text-green-700">Analyzing leaf pathology with Gemini Vision...</div>
        ) : (
          <>
            <div className="flex justify-between items-start">
              <div>
                <div className="text-xs text-gray-500">Detected Condition</div>
                <h3 className="text-xl font-extrabold text-red-700">{selected.disease}</h3>
              </div>
              <span className="bg-green-100 text-green-800 font-extrabold px-3 py-1 rounded-lg">{selected.confidence} Confidence</span>
            </div>
            <div className="bg-green-50 p-4 rounded-xl text-sm"><b>🌿 Organic Treatment:</b> {selected.organic}</div>
            <div className="bg-amber-50 p-4 rounded-xl text-sm"><b>🧪 Chemical Treatment:</b> {selected.chemical}</div>
            <div className="bg-gray-50 p-4 rounded-xl text-sm"><b>🛡️ Prevention Tips:</b> {selected.prevention}</div>
          </>
        )}
      </div>
    </div>
  );
}

function StorePage({ products, addToCart }) {
  const [cat, setCat] = useState('All');
  const filtered = products.filter((p) => cat === 'All' || p.category === cat);

  return (
    <div className="space-y-5">
      <div className="flex flex-wrap gap-2">
        {['All', 'Seeds', 'Fertilizers', 'Pesticides', 'Tools'].map((c) => (
          <button key={c} onClick={() => setCat(c)} className={`px-4 py-2 rounded-xl text-sm font-bold cursor-pointer ${cat === c ? 'bg-green-700 text-white' : 'bg-white border border-green-200'}`}>
            {c}
          </button>
        ))}
      </div>
      <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {filtered.map((p) => (
          <div key={p.id} className="bg-white p-5 rounded-2xl border border-green-100 shadow-sm flex flex-col justify-between">
            <div>
              <div className="flex justify-between items-center mb-2">
                {p.sponsored ? <span className="text-[11px] bg-amber-100 text-amber-800 font-bold px-2 py-0.5 rounded">★ Sponsored</span> : <span />}
                <span className={`text-xs font-bold ${p.inStock ? 'text-green-700' : 'text-red-600'}`}>{p.inStock ? 'In Stock' : 'Out of Stock'}</span>
              </div>
              <h3 className="font-bold text-base">{p.name}</h3>
              <p className="text-xs text-gray-500">{p.seller} • ★ {p.rating}</p>
            </div>
            <div className="mt-4 pt-3 border-t flex justify-between items-center">
              <span className="text-xl font-extrabold text-green-700">₹{p.price}</span>
              <button disabled={!p.inStock} onClick={() => addToCart(p)} className="bg-green-700 hover:bg-green-800 disabled:bg-gray-300 text-white px-4 py-2 rounded-xl text-xs font-bold cursor-pointer">
                Add to Cart
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

function CommunityPage({ posts, setPosts, userName, notify }) {
  const [title, setTitle] = useState('');
  const [body, setBody] = useState('');
  const [replyText, setReplyText] = useState({});

  const addPost = (e) => {
    e.preventDefault();
    if (!title.trim() || !body.trim()) return;
    setPosts([{ id: `p_${Date.now()}`, author: userName, crop: 'General', title, body, replies: [] }, ...posts]);
    setTitle(''); setBody('');
    notify('Question posted to Kisan Chaupal!');
  };

  const addReply = (postId) => {
    const text = replyText[postId];
    if (!text?.trim()) return;
    setPosts(posts.map((p) => (p.id === postId ? { ...p, replies: [...p.replies, { id: `r_${Date.now()}`, author: userName, text }] } : p)));
    setReplyText({ ...replyText, [postId]: '' });
  };

  return (
    <div className="grid lg:grid-cols-3 gap-6">
      <form onSubmit={addPost} className="bg-white p-5 rounded-2xl border border-green-100 shadow-sm space-y-3 h-fit">
        <h2 className="text-lg font-bold">Ask the Farmer Community</h2>
        <input value={title} onChange={(e) => setTitle(e.target.value)} placeholder="Question title..." className="w-full border p-2.5 rounded-xl text-sm" />
        <textarea value={body} onChange={(e) => setBody(e.target.value)} placeholder="Describe your crop question..." rows={4} className="w-full border p-2.5 rounded-xl text-sm" />
        <button type="submit" className="w-full bg-green-700 text-white py-2.5 rounded-xl font-bold text-sm cursor-pointer">Post Question</button>
      </form>

      <div className="lg:col-span-2 space-y-4">
        {posts.map((p) => (
          <div key={p.id} className="bg-white p-5 rounded-2xl border border-green-100 shadow-sm space-y-3">
            <div className="text-xs text-green-700 font-bold">👨‍🌾 {p.author} • {p.crop}</div>
            <h3 className="font-bold text-base">{p.title}</h3>
            <p className="text-sm text-gray-600">{p.body}</p>
            <div className="space-y-2 pt-2 border-t">
              {p.replies.map((r) => (
                <div key={r.id} className="bg-green-50 p-3 rounded-xl text-xs">
                  <b>{r.author}:</b> {r.text}
                </div>
              ))}
              <div className="flex gap-2 pt-1">
                <input
                  value={replyText[p.id] || ''}
                  onChange={(e) => setReplyText({ ...replyText, [p.id]: e.target.value })}
                  placeholder="Write a reply..."
                  className="flex-1 border px-3 py-2 rounded-lg text-xs"
                />
                <button onClick={() => addReply(p.id)} className="bg-green-700 text-white px-4 py-2 rounded-lg text-xs font-bold cursor-pointer">Reply</button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

function DashboardPage({ role, setRole, listings, requests, savedCrops, navigate }) {
  const estRevenue = listings.reduce((s, l) => s + l.qty * l.price, 0);
  const commission = Math.round(estRevenue * 0.05);

  return (
    <div className="space-y-6">
      <div className="flex flex-wrap justify-between items-center gap-3 bg-white p-4 rounded-2xl border border-green-100">
        <div>
          <h2 className="text-xl font-bold">{role} Business Dashboard</h2>
          <p className="text-xs text-gray-500">Switch roles to demo Farmer, Buyer, and Admin analytics</p>
        </div>
        <div className="flex gap-2">
          {['Farmer', 'Buyer', 'Admin'].map((r) => (
            <button key={r} onClick={() => setRole(r)} className={`px-3 py-1.5 rounded-lg text-xs font-bold cursor-pointer ${role === r ? 'bg-green-700 text-white' : 'bg-green-50 text-green-800'}`}>
              {r}
            </button>
          ))}
        </div>
      </div>

      {role === 'Farmer' && (
        <div className="grid sm:grid-cols-3 gap-4">
          <StatCard title="Active Listings" value={listings.length} sub="Total crop lots listed" />
          <StatCard title="Buy Requests" value={requests.length} sub="Direct buyer inquiries" />
          <StatCard title="Estimated Crop Value" value={`₹${estRevenue.toLocaleString()}`} sub="+18% vs middlemen" />
        </div>
      )}

      {role === 'Buyer' && (
        <div className="grid sm:grid-cols-3 gap-4">
          <StatCard title="Purchase Requests Sent" value={requests.length} sub="Active negotiations" />
          <StatCard title="Saved Crop Lots" value={savedCrops.length} sub="Bookmarked listings" />
          <StatCard title="Avg Sourcing Savings" value="12%" sub="Direct farmgate procurement" />
        </div>
      )}

      {role === 'Admin' && (
        <div className="grid sm:grid-cols-4 gap-4">
          <StatCard title="Total Users" value="1,620" sub="1,280 Farmers • 340 Buyers" />
          <StatCard title="Marketplace Listings" value={listings.length} sub="Across 6 districts" />
          <StatCard title="5% Commission Potential" value={`₹${commission.toLocaleString()}`} sub="Platform fee" />
          <StatCard title="Top Selling Crop" value="Wheat & Onion" sub="Highest Qtl volume" />
        </div>
      )}

      <div className="bg-green-800 text-white p-6 rounded-2xl flex flex-wrap justify-between items-center gap-4">
        <div>
          <h3 className="font-bold text-lg">Presentation: AgriSetu Business Model & Impact</h3>
          <p className="text-xs text-green-100">View our 4 revenue streams, problem analysis, and farmer/buyer testimonials.</p>
        </div>
        <button onClick={() => navigate('business')} className="bg-white text-green-900 px-5 py-2.5 rounded-xl font-bold text-sm cursor-pointer">
          Open Business Model
        </button>
      </div>
    </div>
  );
}

function StatCard({ title, value, sub }) {
  return (
    <div className="bg-white p-5 rounded-2xl border border-green-100 shadow-sm">
      <div className="text-xs text-gray-500 font-medium">{title}</div>
      <div className="text-2xl font-extrabold text-green-700 mt-1">{value}</div>
      <div className="text-xs text-gray-500 mt-1">{sub}</div>
    </div>
  );
}

function BusinessModelPage() {
  return (
    <div className="space-y-6">
      <div className="bg-white p-6 rounded-2xl border border-green-100 shadow-sm">
        <h2 className="text-2xl font-extrabold text-green-800 mb-3">Problems We Solve</h2>
        <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-3 text-sm">
          {[
            ['Low Farmer Prices', 'Farmers receive only 30–40% of consumer retail value due to layered commissions.'],
            ['Middlemen Dependency', 'Credit-linked village agents force distress sales immediately after harvest.'],
            ['Lack of Timely Advice', 'Misdiagnosed crop diseases lead to wasted chemical spend and yield loss.'],
            ['Weather Risk', 'Unpredictable rain causes massive post-harvest spoilage without localized alerts.'],
            ['Buyer Sourcing Friction', 'Bulk traders struggle to find graded, farmgate-traceable produce.'],
          ].map(([title, desc]) => (
            <div key={title} className="bg-green-50 p-4 rounded-xl">
              <div className="font-bold text-green-900">{title}</div>
              <p className="text-xs text-gray-700 mt-1">{desc}</p>
            </div>
          ))}
        </div>
      </div>

      <div className="bg-white p-6 rounded-2xl border border-green-100 shadow-sm">
        <h2 className="text-2xl font-extrabold text-green-800 mb-3">AgriSetu Revenue Model</h2>
        <div className="grid sm:grid-cols-2 gap-4 text-sm">
          {[
            ['1. 5% Marketplace Commission', 'Charged on verified crop sales completed between farmers and buyers.'],
            ['2. ₹499/year Krishi Pro Subscription', 'Premium agronomic advisory, 10-day hyper-local alerts, and priority AI support.'],
            ['3. Delivery & Quality Logistics Fee', 'End-to-end farmgate pickup, moisture assaying, and weighbridge coordination.'],
            ['4. Sponsored Input-Store Listings', 'Seed, fertilizer, and tool brands pay for featured placement to active farmers.'],
          ].map(([title, desc]) => (
            <div key={title} className="border border-green-200 p-4 rounded-xl">
              <div className="font-bold text-green-800">{title}</div>
              <p className="text-xs text-gray-600 mt-1">{desc}</p>
            </div>
          ))}
        </div>
      </div>

      <div className="bg-white p-6 rounded-2xl border border-green-100 shadow-sm">
        <h2 className="text-2xl font-extrabold text-green-800 mb-3">Testimonials</h2>
        <div className="grid md:grid-cols-3 gap-4 text-sm">
          {[
            ['Rameshwar Patil (Farmer, Nashik)', '"Selling my 200 quintals of red onion directly on AgriSetu earned me ₹350/quintal more than local brokers."'],
            ['Gurpreet Singh (Farmer, Ludhiana)', '"The Gemini leaf scanner spotted Yellow Rust on my wheat in seconds and saved my entire rabi harvest."'],
            ['Amit Shah (Buyer, FreshOrganics)', '"Direct farmgate sourcing cut our brokerage costs by 12% while giving us transparent quality grading."'],
          ].map(([who, quote]) => (
            <div key={who} className="bg-gray-50 p-4 rounded-xl">
              <div className="font-bold text-green-800 mb-1">{who}</div>
              <p className="text-xs text-gray-700 italic">{quote}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

function LoginPage({ role, setRole, userName, setUserName, navigate, notify }) {
  const [name, setName] = useState(userName);
  const [selRole, setSelRole] = useState(role);

  const handleLogin = (e) => {
    e.preventDefault();
    if (!name.trim()) return;
    setUserName(name);
    setRole(selRole);
    notify(`Signed in as ${name} (${selRole})`);
    navigate('home');
  };

  return (
    <form onSubmit={handleLogin} className="max-w-md mx-auto bg-white p-6 rounded-2xl border border-green-100 shadow-sm space-y-4">
      <h2 className="text-xl font-bold text-green-800">Demo Login / Switch Account</h2>
      <input value={name} onChange={(e) => setName(e.target.value)} placeholder="Your Name" className="w-full border p-3 rounded-xl text-sm" />
      <select value={selRole} onChange={(e) => setSelRole(e.target.value)} className="w-full border p-3 rounded-xl text-sm">
        <option value="Farmer">Farmer</option>
        <option value="Buyer">Buyer</option>
        <option value="Admin">Admin</option>
      </select>
      <button type="submit" className="w-full bg-green-700 text-white py-3 rounded-xl font-bold cursor-pointer">Continue to AgriSetu</button>
    </form>
  );
}

function AdminPage({ users, setUsers, listings, setListings, advisories, setAdvisories, products, setProducts, notify }) {
  const [newAdv, setNewAdv] = useState({ crop: 'Wheat', type: 'Pest Alert', title: '', advice: '' });
  const [newProd, setNewProd] = useState({ name: '', category: 'Seeds', seller: '', price: '' });

  const addAdvisory = (e) => {
    e.preventDefault();
    if (!newAdv.title.trim() || !newAdv.advice.trim()) return;
    setAdvisories([{ id: `adv_${Date.now()}`, ...newAdv }, ...advisories]);
    setNewAdv({ crop: 'Wheat', type: 'Pest Alert', title: '', advice: '' });
    notify('Advisory published');
  };

  const addProduct = (e) => {
    e.preventDefault();
    if (!newProd.name.trim() || Number(newProd.price) <= 0) return;
    setProducts([{ id: `prod_${Date.now()}`, ...newProd, price: Number(newProd.price), rating: 4.8, inStock: true, sponsored: false }, ...products]);
    setNewProd({ name: '', category: 'Seeds', seller: '', price: '' });
    notify('Store product added');
  };

  return (
    <div className="space-y-6">
      <h2 className="text-2xl font-extrabold text-green-800">Admin Operations Panel</h2>
      <div className="grid lg:grid-cols-2 gap-6">
        <div className="bg-white p-5 rounded-2xl border border-green-100 space-y-2">
          <h3 className="font-bold">Manage Users ({users.length})</h3>
          {users.map((u) => (
            <div key={u.id} className="flex justify-between items-center text-sm border-b py-2">
              <span><b>{u.name}</b> ({u.role}) — {u.district}</span>
              <button onClick={() => setUsers(users.map((x) => (x.id === u.id ? { ...x, verified: !x.verified } : x)))} className="text-xs font-bold text-green-700 cursor-pointer">
                {u.verified ? 'Verified ✓' : 'Verify'}
              </button>
            </div>
          ))}
        </div>

        <div className="bg-white p-5 rounded-2xl border border-green-100 space-y-2">
          <h3 className="font-bold">Manage Listings ({listings.length})</h3>
          {listings.map((l) => (
            <div key={l.id} className="flex justify-between items-center text-sm border-b py-2">
              <span>{l.crop} ({l.qty} Qtl) — {l.status}</span>
              <button onClick={() => { setListings(listings.filter((x) => x.id !== l.id)); notify('Listing removed'); }} className="text-xs text-red-600 font-bold cursor-pointer">
                Remove
              </button>
            </div>
          ))}
        </div>

        <form onSubmit={addAdvisory} className="bg-white p-5 rounded-2xl border border-green-100 space-y-3">
          <h3 className="font-bold">Manage Advisories ({advisories.length})</h3>
          <input placeholder="Advisory Title" value={newAdv.title} onChange={(e) => setNewAdv({ ...newAdv, title: e.target.value })} className="w-full border p-2 rounded-lg text-sm" />
          <input placeholder="Advice text" value={newAdv.advice} onChange={(e) => setNewAdv({ ...newAdv, advice: e.target.value })} className="w-full border p-2 rounded-lg text-sm" />
          <button type="submit" className="bg-green-700 text-white px-4 py-2 rounded-lg text-xs font-bold cursor-pointer">Add Advisory</button>
        </form>

        <form onSubmit={addProduct} className="bg-white p-5 rounded-2xl border border-green-100 space-y-3">
          <h3 className="font-bold">Manage Input Store Products ({products.length})</h3>
          <input placeholder="Product Name" value={newProd.name} onChange={(e) => setNewProd({ ...newProd, name: e.target.value })} className="w-full border p-2 rounded-lg text-sm" />
          <div className="grid grid-cols-2 gap-2">
            <input placeholder="Seller" value={newProd.seller} onChange={(e) => setNewProd({ ...newProd, seller: e.target.value })} className="border p-2 rounded-lg text-sm" />
            <input type="number" placeholder="Price (₹)" value={newProd.price} onChange={(e) => setNewProd({ ...newProd, price: e.target.value })} className="border p-2 rounded-lg text-sm" />
          </div>
          <button type="submit" className="bg-green-700 text-white px-4 py-2 rounded-lg text-xs font-bold cursor-pointer">Add Product</button>
        </form>
      </div>
    </div>
  );
}

function CartModal({ cart, setCart, onClose, notify }) {
  const [confirmed, setConfirmed] = useState(false);
  const total = cart.reduce((s, i) => s + i.price * i.qty, 0);

  return (
    <div className="fixed inset-0 z-50 bg-black/50 flex justify-end">
      <div className="bg-white w-full max-w-md h-full p-5 flex flex-col justify-between">
        <div>
          <div className="flex justify-between items-center border-b pb-3">
            <h3 className="font-bold text-lg">Agri-Input Cart</h3>
            <button onClick={onClose}><X /></button>
          </div>
          {confirmed ? (
            <div className="py-12 text-center space-y-2">
              <div className="w-12 h-12 rounded-full bg-green-100 text-green-700 flex items-center justify-center mx-auto"><Check /></div>
              <div className="font-bold text-lg text-green-800">Order Confirmed (Simulated)!</div>
              <p className="text-xs text-gray-500">Order #AGRI-{Date.now() % 10000} will be delivered within 48 hours.</p>
            </div>
          ) : cart.length === 0 ? (
            <p className="py-12 text-center text-sm text-gray-500">Your cart is empty.</p>
          ) : (
            <div className="space-y-3 mt-4">
              {cart.map((i) => (
                <div key={i.id} className="flex justify-between items-center bg-green-50 p-3 rounded-xl text-sm">
                  <div>
                    <div className="font-bold">{i.name}</div>
                    <div className="text-xs text-green-700">₹{i.price} × {i.qty}</div>
                  </div>
                  <div className="flex items-center gap-2">
                    <button onClick={() => setCart(cart.map((c) => (c.id === i.id ? { ...c, qty: Math.max(0, c.qty - 1) } : c)).filter((c) => c.qty > 0))} className="px-2 bg-white rounded border">-</button>
                    <span>{i.qty}</span>
                    <button onClick={() => setCart(cart.map((c) => (c.id === i.id ? { ...c, qty: c.qty + 1 } : c)))} className="px-2 bg-white rounded border">+</button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {!confirmed && cart.length > 0 && (
          <div className="border-t pt-4 space-y-3">
            <div className="flex justify-between font-extrabold text-lg">
              <span>Total</span>
              <span className="text-green-700">₹{total}</span>
            </div>
            <button
              onClick={() => { setConfirmed(true); setCart([]); notify('Order confirmed!'); }}
              className="w-full bg-green-700 text-white py-3 rounded-xl font-bold cursor-pointer"
            >
              Confirm Order (Simulated)
            </button>
          </div>
        )}
      </div>
    </div>
  );
}

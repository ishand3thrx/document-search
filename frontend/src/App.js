import React, { useState, useEffect } from 'react';
import './app.css';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

function App() {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const [doc, setDoc] = useState({ id: '', title: '', content: '' });
  const [submitting, setSubmitting] = useState(false);
  const [message, setMessage] = useState('');

  useEffect(() => {
    const timer = setTimeout(() => {
      if (query.trim()) {
        searchDocs(query);
      } else {
        setResults([]);
      }
    }, 300);
    return () => clearTimeout(timer);
  }, [query]);

  const searchDocs = async (q) => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch(`${API_URL}/search?q=${encodeURIComponent(q)}`);
      const data = await res.json();
      setResults(data.documents || []);
    } catch (err) {
      setError('Search failed');
      setResults([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!doc.id.trim() || !doc.title.trim() || !doc.content.trim()) {
      setMessage('All fields required');
      return;
    }

    setSubmitting(true);
    setMessage('');

    try {
      const res = await fetch(`${API_URL}/index`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(doc)
      });

      if (res.ok) {
        setMessage('Document added');
        setDoc({ id: '', title: '', content: '' });
        setTimeout(() => setMessage(''), 3000);
      } else {
        setMessage('Failed to add document');
      }
    } catch (err) {
      setMessage('Connection error');
    } finally {
      setSubmitting(false);
    }
  };

  const updateDoc = (field, val) => {
    setDoc(prev => ({ ...prev, [field]: val }));
  };

  return (
    <div className="app">
      <header className="header">
        <h1>Document Search</h1>
        <p>Search and index documents</p>
      </header>

      <main className="container">
        <section className="add-section">
          <h2>Add Document</h2>
          <form onSubmit={handleSubmit}>
            <input
              type="text"
              placeholder="ID"
              value={doc.id}
              onChange={(e) => updateDoc('id', e.target.value)}
              disabled={submitting}
            />
            <input
              type="text"
              placeholder="Title"
              value={doc.title}
              onChange={(e) => updateDoc('title', e.target.value)}
              disabled={submitting}
            />
            <textarea
              placeholder="Content"
              value={doc.content}
              onChange={(e) => updateDoc('content', e.target.value)}
              rows="4"
              disabled={submitting}
            />
            <button type="submit" disabled={submitting}>
              {submitting ? 'Adding...' : 'Add Document'}
            </button>
            {message && <div className={`msg ${message.includes('added') ? 'success' : 'error'}`}>{message}</div>}
          </form>
        </section>

        <section className="search-section">
          <h2>Search</h2>
          <input
            type="text"
            placeholder="Search documents..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            className="search-input"
          />

          <div className="results">
            {loading && <p>Loading...</p>}
            {error && <p className="error">{error}</p>}
            {!loading && !error && results.length === 0 && query && (
              <p>No results for "{query}"</p>
            )}
            {results.length > 0 && (
              <>
                <h3>{results.length} Results</h3>
                {results.map(d => (
                  <div key={d.id} className="card">
                    <h4>{d.title}</h4>
                    <small>ID: {d.id}</small>
                    <p>{d.content.substring(0, 200)}{d.content.length > 200 ? '...' : ''}</p>
                  </div>
                ))}
              </>
            )}
          </div>
        </section>
      </main>
    </div>
  );
}

export default App;

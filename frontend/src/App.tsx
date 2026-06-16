import { useEffect, useState } from 'react'
import type { Kvoteresultat } from './types/Kvoteresultat'
import Bruddtabell from './components/Bruddtabell'

function App() {
  const [kvoter, setKvoter] = useState<Kvoteresultat[]>([])
  const [laster, setLaster] = useState(true)
  const [feil, setFeil] = useState<string | null>(null)

  useEffect(() => {
    fetch('/api/kvote')
      .then((res) => {
        if (!res.ok) throw new Error(`HTTP ${res.status}`)
        return res.json()
      })
      .then((data: Kvoteresultat[]) => setKvoter(data))
      .catch((e) => setFeil(e.message))
      .finally(() => setLaster(false))
  }, [])

  if (laster) return <p>Laster …</p>
  if (feil) return <p>Noe gikk galt: {feil}</p>

  return (
    <div style={{ fontFamily: 'system-ui', padding: '2rem' }}>
      <h1>Fleksibilitetskvote – Q2 2026</h1>
      {kvoter.map((k) => (
        <div
          key={k.segment}
          style={{
            border: '1px solid #ddd',
            borderRadius: 8,
            padding: '1rem',
            marginBottom: '1rem',
          }}
        >
          <h2>{k.segment}</h2>
          <p>
            Bruddandel: {(k.bruddAndel * 100).toFixed(1)} % av {(k.grense * 100).toFixed(0)} % tillatt
          </p>
          <p style={{ color: k.overGrense ? 'crimson' : 'green', fontWeight: 600 }}>
            {k.overGrense ? '⚠ Over kvoten' : '✓ Innenfor kvoten'}
          </p>
          <small>
            {k.antallLan} lån · bruddvolum {k.bruddVolum.toLocaleString('nb-NO')} kr av{' '}
            {k.totalVolum.toLocaleString('nb-NO')} kr
          </small>
        </div>
      ))}
      <Bruddtabell />
    </div>
  )
}

export default App
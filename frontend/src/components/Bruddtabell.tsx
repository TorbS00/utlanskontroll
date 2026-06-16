import { useEffect, useState } from 'react'
import type { Lan } from '../types/Lan'

function bruddTyper(lan: Lan): string[] {
  const typer: string[] = []
  if (lan.bruddLikviditet) typer.push('Likviditet')
  if (lan.bruddGjeldsgrad) typer.push('Gjeldsgrad')
  if (lan.bruddLtv)        typer.push('LTV')
  if (lan.bruddAvdrag)     typer.push('Avdrag')
  return typer
}

function formaterDato(iso: string): string {
  const [aar, maaned, dag] = iso.split('-')
  return `${dag}.${maaned}.${aar}`
}

function Bruddtabell() {
  const [lan, setLan] = useState<Lan[]>([])
  const [laster, setLaster] = useState(true)
  const [feil, setFeil] = useState<string | null>(null)
  const [visKunBrudd, setVisKunBrudd] = useState(false)

  useEffect(() => {
    fetch('/api/lan')
      .then((res) => {
        if (!res.ok) throw new Error(`HTTP ${res.status}`)
        return res.json()
      })
      .then((data: Lan[]) => setLan(data))
      .catch((e) => setFeil(e.message))
      .finally(() => setLaster(false))
  }, [])

  if (laster) return <p>Laster lån …</p>
  if (feil) return <p>Noe gikk galt: {feil}</p>

  const antallMedBrudd = lan.filter((l) => bruddTyper(l).length > 0).length
  const synligeLan = visKunBrudd
    ? lan.filter((l) => bruddTyper(l).length > 0)
    : lan

  return (
    <div style={{ marginTop: '2rem' }}>
      <h2>Lån – Q2 2026</h2>
      <p>
        {antallMedBrudd} av {lan.length} lån har minst ett brudd.{' '}
        <button onClick={() => setVisKunBrudd(!visKunBrudd)}>
          {visKunBrudd ? 'Vis alle' : 'Vis kun brudd'}
        </button>
      </p>

      <table style={{ borderCollapse: 'collapse', width: '100%' }}>
        <thead>
          <tr style={{ textAlign: 'left', borderBottom: '2px solid #333' }}>
            <th style={{ padding: '0.5rem' }}>Kundenr</th>
            <th style={{ padding: '0.5rem' }}>Dato</th>
            <th style={{ padding: '0.5rem', textAlign: 'right' }}>Beløp</th>
            <th style={{ padding: '0.5rem' }}>Område</th>
            <th style={{ padding: '0.5rem' }}>Brudd</th>
          </tr>
        </thead>
        <tbody>
          {synligeLan.map((l) => {
            const typer = bruddTyper(l)
            const harBrudd = typer.length > 0
            return (
              <tr
                key={l.kundenr}
                style={{
                  backgroundColor: harBrudd ? '#fde8e8' : 'transparent',
                  borderBottom: '1px solid #eee',
                }}
              >
                <td style={{ padding: '0.5rem' }}>{l.kundenr}</td>
                <td style={{ padding: '0.5rem' }}>{formaterDato(l.bevilgetDato)}</td>
                <td style={{ padding: '0.5rem', textAlign: 'right' }}>
                  {l.bevilgetBelop.toLocaleString('nb-NO')} kr
                </td>
                <td style={{ padding: '0.5rem' }}>{l.oslo ? 'Oslo' : 'Resten'}</td>
                <td style={{ padding: '0.5rem', color: 'crimson' }}>
                  {harBrudd ? typer.join(', ') : '—'}
                </td>
              </tr>
            )
          })}
        </tbody>
      </table>
    </div>
  )
}

export default Bruddtabell
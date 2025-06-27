import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import axios from 'axios'
import './App.css'

function App() {
  const [x1, setX1] = useState(0)
  const [x2, setX2] = useState(0)
  const [x3, setX3] = useState(0)
    const [y1, setY1] = useState(0)
    const [y2, setY2] = useState(0)
    const [y3, setY3] = useState(0)


    const api = axios.create({
        baseURL: 'http://localhost:8080',
        headers: {
            'Content-Type': 'application/json',
        },
    });

  function handleSubmit(){
      if((x1 === x2 && x2 === x3) ||(y1 === y2 && y2 === y3) ){
          const p1 = (x1 - 1) * 5 + y1 - 1;
          const p2 = (x2 - 1) * 5 + y2 - 1;
          const p3 = (x3 - 1) * 5 + y3 - 1;

          const data = {
              poz1 : p1,
              poz2: p2,
              poz3: p3
          }

          api.post("/joc", data)
              .then(() => alert("Barcuta added successfully!"))
              .catch(() => alert("Error adding barcuta :("))
      }

      else {
          alert("The positions are not situated on the same row or column :(")
      }

  }

  return (
      <>
        <h1>
          INTRODUCETI COORDONATELE NOII BARCI:
        </h1>
        <p>Pozitia 1</p>
        <input type="number" min={1} max={5} onChange={e => setX1(parseInt(e.target.value))}/>
        <input type="number" min={1} max={5} onChange={e => setY1(parseInt(e.target.value))}/>
        <p>Pozitia 2</p>
        <input type="number" min={1} max={5} onChange={e => setX2(parseInt(e.target.value))}/>
        <input type="number" min={1} max={5} onChange={e => setY2(parseInt(e.target.value))}/>
        <p>Pozitia 3</p>
        <input type="number" min={1} max={5} onChange={e => setX3(parseInt(e.target.value))}/>
        <input type="number" min={1} max={5} onChange={e => setY3(parseInt(e.target.value))}/>

        <button onClick={handleSubmit}>SUBMIT</button>
      </>
  )
}

export default App

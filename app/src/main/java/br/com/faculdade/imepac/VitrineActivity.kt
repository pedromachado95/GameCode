package br.com.faculdade.imepac

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class VitrineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vitrine)
        supportActionBar?.hide()

        val btR6 = findViewById<Button>(R.id.bt_comprar_r6)
        val btSf6 = findViewById<Button>(R.id.bt_comprar_sf6)
        val btMine = findViewById<Button>(R.id.bt_comprar_mine)
        val btLol = findViewById<Button>(R.id.bt_comprar_lol)
        val btGta = findViewById<Button>(R.id.bt_comprar_gta5)
        val btElden = findViewById<Button>(R.id.bt_comprar_elden)
        val btRdr2 = findViewById<Button>(R.id.bt_comprar_rdr2)
        val btCyber = findViewById<Button>(R.id.bt_comprar_cyber)
        val btRe4 = findViewById<Button>(R.id.bt_comprar_re4)
        val btCod = findViewById<Button>(R.id.bt_comprar_cod)

        btR6.setOnClickListener { realizarCompra("Rainbow Six Siege", "R$ 39,90") }
        btSf6.setOnClickListener { realizarCompra("Street Fighter 6", "R$ 249,00") }
        btMine.setOnClickListener { realizarCompra("Minecraft", "R$ 149,00") }
        btLol.setOnClickListener { realizarCompra("League of Legends - 1380 RP", "R$ 34,90") }
        btGta.setOnClickListener { realizarCompra("Grand Theft Auto V", "R$ 82,00") }
        btElden.setOnClickListener { realizarCompra("Elden Ring", "R$ 229,90") }
        btRdr2.setOnClickListener { realizarCompra("Red Dead Redemption 2", "R$ 119,90") }
        btCyber.setOnClickListener { realizarCompra("Cyberpunk 2077", "R$ 199,90") }
        btRe4.setOnClickListener { realizarCompra("Resident Evil 4 Remake", "R$ 169,00") }
        btCod.setOnClickListener { realizarCompra("Call of Duty: MW III", "R$ 299,00") }
    }

    private fun realizarCompra(nomeJogo: String, preco: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("NOME_JOGO", nomeJogo)
        intent.putExtra("PRECO_JOGO", preco)
        startActivity(intent)
    }
}
package br.com.faculdade.imepac

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()

        val btCatalogo = findViewById<Button>(R.id.bt_ir_catalogo)
        val btBiblioteca = findViewById<Button>(R.id.bt_ir_biblioteca)

        // Botão que leva o usuário para ver os jogos à venda
        btCatalogo.setOnClickListener {
            val intent = Intent(this, VitrineActivity::class.java)
            startActivity(intent)
        }

        // Botão da biblioteca ativado!
        btBiblioteca.setOnClickListener {
            val intent = Intent(this, BibliotecaActivity::class.java)
            startActivity(intent)
        }
    }
}
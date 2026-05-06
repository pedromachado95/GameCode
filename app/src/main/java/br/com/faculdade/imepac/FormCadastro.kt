package br.com.faculdade.imepac

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class FormCadastro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_cadastro)

        // Esconde a barra superior
        supportActionBar?.hide()
    }
}
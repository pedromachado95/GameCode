package br.com.faculdade.imepac

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class DetalhesCompraActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private var idCompra: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_compra)
        supportActionBar?.hide()

        idCompra = intent.getStringExtra("ID_COMPRA")

        if (idCompra != null) {
            buscarDetalhesDaCompra()
        } else {
            Toast.makeText(this, "Erro ao carregar a compra", Toast.LENGTH_SHORT).show()
            finish()
        }

        val btResgatar = findViewById<Button>(R.id.bt_resgatar_key)
        val btExcluir = findViewById<Button>(R.id.bt_excluir_historico)

        // Botão de Update
        btResgatar.setOnClickListener {
            atualizarStatusResgate()
        }

        // Botão de Delete
        btExcluir.setOnClickListener {
            excluirCompra()
        }
    }

    private fun buscarDetalhesDaCompra() {
        val txtNome = findViewById<TextView>(R.id.txt_detalhe_nome)
        val txtKey = findViewById<TextView>(R.id.txt_detalhe_key)
        val txtStatus = findViewById<TextView>(R.id.txt_detalhe_status)
        val btResgatar = findViewById<Button>(R.id.bt_resgatar_key)
        val btExcluir = findViewById<Button>(R.id.bt_excluir_historico)

        db.collection("Compras").document(idCompra!!)
            .get()
            .addOnSuccessListener { documento ->
                if (documento != null && documento.exists()) {
                    txtNome.text = documento.getString("nome_jogo")
                    txtKey.text = documento.getString("chave_ativacao")

                    val statusAtual = documento.getString("status")
                    txtStatus.text = "Status: $statusAtual"

                    // REGRA DE NEGÓCIO: Alterna os botões dependendo do status
                    if (statusAtual == "Resgatada") {
                        btResgatar.visibility = View.GONE     // Esconde o botão de resgate
                        btExcluir.visibility = View.VISIBLE   // Mostra o botão de excluir
                    } else {
                        btResgatar.visibility = View.VISIBLE
                        btExcluir.visibility = View.GONE
                    }
                }
            }
    }

    private fun atualizarStatusResgate() {
        db.collection("Compras").document(idCompra!!)
            .update("status", "Resgatada")
            .addOnSuccessListener {
                Toast.makeText(this, "Key marcada como resgatada!", Toast.LENGTH_SHORT).show()
                buscarDetalhesDaCompra() // Recarrega a tela para atualizar os botões
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao atualizar status", Toast.LENGTH_SHORT).show()
            }
    }

    // A CEREJA DO BOLO: Letra "D" do CRUD (Delete)
    private fun excluirCompra() {
        db.collection("Compras").document(idCompra!!)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Registro apagado do histórico!", Toast.LENGTH_LONG).show()
                finish() // Fecha a tela e volta para a biblioteca limpa
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao apagar registro do banco", Toast.LENGTH_SHORT).show()
            }
    }
}
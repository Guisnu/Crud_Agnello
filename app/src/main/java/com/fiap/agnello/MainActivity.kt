package com.fiap.agnello

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fiap.agnello.dataset.repository.VinhoRepository
import com.fiap.agnello.model.Vinho
import com.fiap.agnello.ui.theme.AgnneloAppTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgnneloAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        CadastroScreen()
                    }
                }
            }
        }
    }
}

val tiposVinhos = listOf("Rosé", "Suave", "Seco", "Tinto", "Branco")

@Composable
fun CadastroScreen() {
    var nomeState = remember { mutableStateOf("") }
    var tipoState = remember { mutableStateOf(tiposVinhos[0]) }
    var precoState = remember { mutableStateOf(0.00) }

    val context = LocalContext.current
    val vinhoRepository = VinhoRepository(context)

    var VinhoListState = remember { mutableStateOf(vinhoRepository.listarVinhos()) }
    var editandoState = remember { mutableStateOf<Vinho?>(null) }


    Column {
        VinhoForm(
            nome = nomeState.value,
            tipo = tipoState.value,
            preco = precoState.value,
            onNomeChange = { nomeState.value = it },
            onTipoChange = { tipoState.value = it },
            onPrecoChange = { precoState.value = it },
            atualizar = {
                VinhoListState.value = vinhoRepository.listarVinhos()
                editandoState.value = null
            },
            vinhoEditando = editandoState.value
        )
        VinhoList(
            VinhoListState,
            atualizar = { VinhoListState.value = vinhoRepository.listarVinhos() },
            onClick = { vinho ->
                nomeState.value = vinho.nome
                tipoState.value = vinho.tipo
                precoState.value = vinho.preco
                editandoState.value = vinho
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VinhoForm(
    nome: String,
    tipo: String,
    preco: Double,
    onNomeChange: (String) -> Unit,
    onTipoChange: (String) -> Unit,
    onPrecoChange: (Double) -> Unit,
    atualizar: () -> Unit,
    vinhoEditando: Vinho?
) {
    var expanded by remember { mutableStateOf(false) }
    var tipoSelecionado by remember { mutableStateOf(tiposVinhos[0]) }

    // Obtendo contexto
    val context = LocalContext.current
    val vinhoRepository = VinhoRepository(context)

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Agnello",
            fontSize = 50.sp,
            fontFamily = FontFamily.Cursive,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = nome,
            onValueChange = onNomeChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Nome do Vinho") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = tipoSelecionado,
                onValueChange = { },
                readOnly = true,
                label = { Text("Tipo de vinho") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                tiposVinhos.forEach { tipo ->
                    DropdownMenuItem(
                        text = { Text(tipo) },
                        onClick = {
                            tipoSelecionado = tipo
                            onTipoChange(tipo)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        CampoPreco(
            value = preco,
            onValueChange = onPrecoChange,
            label = "Preço do Vinho"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (vinhoEditando == null) {
                    vinhoRepository.salvar(Vinho(0, nome, tipo, preco))
                } else {
                    vinhoRepository.atualizar(
                        Vinho(
                            id = vinhoEditando.id,
                            nome = nome,
                            tipo = tipo,
                            preco = preco
                        )
                    )
                }
                atualizar()
                onNomeChange("")
                onTipoChange(tiposVinhos[0])
                onPrecoChange(0.0)
            },
            enabled = nome.isNotBlank() && preco > 0.0,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text(text = if (vinhoEditando == null) "CADASTRAR" else "ATUALIZAR")
        }

    }
}

@Composable
fun CampoPreco(
    value: Double,
    onValueChange: (Double) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Preço do Vinho"
) {
    var textValue by remember(value) {
        mutableStateOf(
            TextFieldValue(
                text = formatMonetary(value),
                selection = TextRange(formatMonetary(value).length)
            )
        )
    }

    LaunchedEffect(value) {
        val formatted = formatMonetary(value)
        if (textValue.text != formatted) {
            textValue = TextFieldValue(
                text = formatted,
                selection = TextRange(formatted.length)
            )
        }
    }

    OutlinedTextField(
        value = textValue,
        onValueChange = { fieldValue ->
            val digito = fieldValue.text.filter { it.isDigit() }

            val digitoDouble = if (digito.isEmpty()) 0.0 else digito.toLong() / 100.0
            onValueChange(digitoDouble)

            val newFormatted = formatMonetary(digitoDouble)

            textValue = TextFieldValue(
                text = newFormatted,
                selection = TextRange(newFormatted.length)
            )
        },
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        placeholder = { Text("0,00") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}

private fun formatMonetary(value: Double): String =
    String.format(Locale("pt", "BR"), "%,.2f", value)
        .replace(Regex("[^0-9,.]"), "")

@Composable
fun VinhoList(
    ListaVinhos: MutableState<List<Vinho>>,
    atualizar: () -> Unit,
    onClick: (Vinho) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        for (vinho in ListaVinhos.value) {
            VinhoCard(vinho, atualizar, onClick)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun VinhoCard(vinho: Vinho, atualizar: () -> Unit, onClick: (Vinho) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray
        )
    ) {
        val context = LocalContext.current
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(2f)
            ) {
                Text(
                    text = "Nome: " + vinho.nome,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tipo: " + vinho.tipo,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "R$ " + vinho.preco.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(onClick = {
                val vinhoRepository = VinhoRepository(context = context)
                vinhoRepository.excluir(vinho)
                atualizar()
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )
            }
            IconButton(onClick = {
                onClick(vinho)
            }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
            }
        }
    }
}
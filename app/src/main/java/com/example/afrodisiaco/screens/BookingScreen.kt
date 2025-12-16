package com.example.afrodisiaco.screens

import com.example.afrodisiaco.MainViewModel
import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Estado del formulario
    var selectedDateText by remember { mutableStateOf("") }
    var selectedPackage by remember { mutableStateOf("") }
    var eventType by remember { mutableStateOf("") }
    var guests by remember { mutableStateOf("") }
    var specialRequests by remember { mutableStateOf("") }

    // Estado para los Dropdowns (Menús desplegables)
    var expandedPackage by remember { mutableStateOf(false) }
    var expandedEvent by remember { mutableStateOf(false) }

    // Configuración del Calendario
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            selectedDateText = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.datePicker.minDate = System.currentTimeMillis() // No fechas pasadas

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = viewModel.t("bookYourEvent"),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = viewModel.t("confirmBooking"),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

                // --- FECHA (Abre el diálogo) ---
                OutlinedTextField(
                    value = selectedDateText,
                    onValueChange = { },
                    label = { Text(viewModel.t("selectDate")) },
                    readOnly = true, // Importante: No se escribe, se selecciona
                    trailingIcon = {
                        Icon(Icons.Default.CalendarToday, contentDescription = null,
                            modifier = Modifier.clickable { datePickerDialog.show() })
                    },
                    modifier = Modifier.fillMaxWidth().clickable { datePickerDialog.show() },
                    enabled = false, // Deshabilitamos la edición manual
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                // --- SELECTOR DE PAQUETE ---
                ExposedDropdownMenuBox(
                    expanded = expandedPackage,
                    onExpandedChange = { expandedPackage = !expandedPackage }
                ) {
                    OutlinedTextField(
                        value = selectedPackage.ifEmpty { viewModel.t("selectPackage2") },
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPackage) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedPackage,
                        onDismissRequest = { expandedPackage = false }
                    ) {
                        listOf("basicPackage", "standardPackage", "premiumPackage").forEach { key ->
                            DropdownMenuItem(
                                text = { Text(viewModel.t(key)) },
                                onClick = {
                                    selectedPackage = viewModel.t(key)
                                    expandedPackage = false
                                }
                            )
                        }
                    }
                }

                // --- TIPO DE EVENTO ---
                ExposedDropdownMenuBox(
                    expanded = expandedEvent,
                    onExpandedChange = { expandedEvent = !expandedEvent }
                ) {
                    OutlinedTextField(
                        value = eventType.ifEmpty { viewModel.t("eventType") },
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEvent) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedEvent,
                        onDismissRequest = { expandedEvent = false }
                    ) {
                        listOf("wedding", "birthday", "corporate", "other").forEach { key ->
                            DropdownMenuItem(
                                text = { Text(viewModel.t(key)) },
                                onClick = {
                                    eventType = viewModel.t(key)
                                    expandedEvent = false
                                }
                            )
                        }
                    }
                }

                // --- INVITADOS ---
                OutlinedTextField(
                    value = guests,
                    onValueChange = { guests = it },
                    label = { Text(viewModel.t("guests")) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                // --- PETICIONES ESPECIALES ---
                OutlinedTextField(
                    value = specialRequests,
                    onValueChange = { specialRequests = it },
                    label = { Text(viewModel.t("specialRequests")) },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    singleLine = false
                )

                Button(
                    onClick = {
                        if (selectedDateText.isNotEmpty() && selectedPackage.isNotEmpty()) {
                            Toast.makeText(context, "¡Reservación Exitosa!", Toast.LENGTH_LONG).show()
                            // Limpiar campos
                            selectedDateText = ""
                            selectedPackage = ""
                            guests = ""
                            specialRequests = ""
                        } else {
                            Toast.makeText(context, viewModel.t("error"), Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedDateText.isNotEmpty()
                ) {
                    Text(viewModel.t("confirmBooking"))
                }
            }
        }
    }
}
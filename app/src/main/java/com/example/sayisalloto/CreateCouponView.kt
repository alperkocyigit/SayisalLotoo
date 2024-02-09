package com.example.sayisalloto

import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sayisalloto.extension.LimitedLengthVisualTransformation
import com.example.sayisalloto.viewmodel.CreateCouponViewModel
import com.example.sayisalloto.viewmodelfactory.CreateCouponViewModelFactory

@Composable
fun NumberSheet(onDismiss: (Set<Int>) -> Unit,onDismissRequest: () -> Unit) {
    var selectedNumbers by remember { mutableStateOf<Set<Int>>(emptySet()) }
    Dialog(
        onDismissRequest = { onDismissRequest() } // pop up sayfasının dışına dokunduğumda kapanmasını sağlayan yapı
    ) {
        Surface(
            color = Color.White,
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            border = BorderStroke(1.dp,Color.Black),
            shape = CircleShape.copy(all = CornerSize(15.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Lütfen 1 ile 49 arasında kuponunuza eklemek istediğiniz 6 adet sayı seçiniz.",
                    modifier = Modifier
                        .padding(all = 5.dp)
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                NumberGrid(rows = 7, columns = 7,selectedNumbers = selectedNumbers) { number ->
                    selectedNumbers = if (selectedNumbers.contains(number)) {
                        selectedNumbers - number
                    } else {
                        if (selectedNumbers.size < 6) {
                            selectedNumbers + number
                        } else {
                            selectedNumbers
                        }
                    }
                }
                Row(modifier = Modifier.padding(top = 20.dp)){
                    Button(onClick = {onDismissRequest()}) {
                        Text(text = "İptal")
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    Button(onClick = {
                        onDismiss(selectedNumbers.toSortedSet())
                        onDismissRequest()
                                     },enabled = selectedNumbers.size == 6) {

                        Text(text = "Kaydet")

                    }
                }
            }
        }
    }
}
// Pop up sayfasında oluşturulan 1 den 49 a kadar olan sayıların grid tasarımı
@Composable
fun NumberGrid(rows: Int, columns: Int,selectedNumbers: Set<Int>,onItemClick: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(rows) { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val startNumber = row * columns + 1
                val endNumber = startNumber + columns - 1
                for (number in startNumber..endNumber) {
                    val isSelected = selectedNumbers.contains(number)
                    val textColor = if (isSelected) Color.White else Color.Black
                    val backgroundColor = if (isSelected) Color.Blue else Color.LightGray
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clickable { onItemClick(number) }
                            .background(color = backgroundColor),
                        contentAlignment = Alignment.Center,

                    ) {
                        Text(
                            text = number.toString(),
                            style = TextStyle(color = textColor),
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun BackButton(navigationController: NavController) {
    IconButton(onClick = { navigationController.navigateUp() }) {
        Icon(Icons.Filled.ArrowBack, contentDescription = "")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp(navigationController: NavController) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedColumns by remember { mutableStateOf(emptyList<List<Int>>()) }
    var boxHeight by remember { mutableStateOf(60.dp) }
    var couponName by remember { mutableStateOf("") } // Kupon adı için değişken


    val context = LocalContext.current
    val viewModel: CreateCouponViewModel = viewModel(
        factory = CreateCouponViewModelFactory(context.applicationContext as Application)
    )


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row {
                        BackButton(navigationController = navigationController )
                        Spacer(modifier = Modifier)
                        Text(text = "YENİ KUPON", modifier = Modifier.padding(top = 9.dp))
                    }

                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(top = 35.dp)
                    .padding(bottom = 50.dp)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                // Dikdörtgen boş bir kart oluşturuldu.
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(boxHeight)
                        .border(width = 1.dp, color = Color.Black)
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                        color = Color.White,
                    ) {
                        Column(
                            modifier = Modifier,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(color = Color.Gray)
                                    .height(60.dp))
                            {
                                Text("Seçilen Kolonlar", fontSize = 20.sp, textAlign = TextAlign.Center ,modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp)
                                )
                            }
                            selectedColumns.forEachIndexed { index, column ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Kolon ${index + 1}:")
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        column.forEach { number ->
                                            Box(modifier = Modifier
                                                .clip(shape = CircleShape)
                                                .background(color = Color.Black)
                                                .width(28.dp)
                                                .height(28.dp)){
                                                Text(
                                                    text = number.toString(),
                                                    color = Color.White,
                                                    modifier = Modifier
                                                        .padding(2.dp)
                                                        .padding(start = 3.dp)

                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(onClick = {
                                        selectedColumns = selectedColumns.toMutableList().also { it.removeAt(index) }
                                        boxHeight -= 68.dp
                                    }) {
                                        Text("Sil")
                                    }
                                }
                                Divider(modifier = Modifier
                                    .padding(vertical = 2.dp)
                                    .background(color = Color.Black))
                            }
                        }
                    }
                }
                Button(onClick = { showDialog = true }) {
                    Text(text = "Kolon Ekle")
                }
                if (showDialog) {
                    NumberSheet(
                        onDismiss = { colon ->
                            selectedColumns += mutableListOf(colon.toList()) //number sheet sayfasında kaydettiğimiz set yapıyı burada listeye dönüştürüp ekledik.
                            boxHeight += 68.dp
                        },
                        onDismissRequest = { showDialog = false }
                    )
                }
                OutlinedTextField( modifier = Modifier.width(325.dp),
                    value = couponName,
                    onValueChange = { if(it.length <= 20){
                        couponName = it
                    }
                        },
                    maxLines = 1,
                    visualTransformation = LimitedLengthVisualTransformation(20),
                    label = { Text("Kupon Adı") },
                    placeholder = {Text(text = "Maksimum 20 karakter", color = Color.LightGray)}
                )

            }
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RectangleShape,
                onClick = {
                    Toast.makeText(context, "'${couponName}' Kuponunuz Kaydedildi.", Toast.LENGTH_LONG).show()
                    val coupon_name = couponName
                    val coupon_colon = selectedColumns
                    viewModel.register(coupon_name,coupon_colon)
                    navigationController.navigateUp()
                },
                enabled = selectedColumns.isNotEmpty() && couponName.isNotEmpty()
            ) {
                Text(text = "Kuponu Kaydet")
            }
        }


    )
}
@Preview
@Composable
fun PreviewApp() {
    MaterialTheme {

    }
}
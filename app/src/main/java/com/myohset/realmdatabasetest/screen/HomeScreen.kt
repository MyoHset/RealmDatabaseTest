package com.myohset.realmdatabasetest.screen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.myohset.realmdatabasetest.model.Person
import io.realm.kotlin.types.RealmInstant
import java.lang.reflect.Modifier
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen (

    data: List<Person>,
    filtered: Boolean,
    name: String,
    objectId: String,
    onNameChanged: (String) -> Unit,
    onObjectIdChanged: (String) -> Unit,
    onInsertClicked: () -> Unit,
    onUpdateClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onFilterClicked: () -> Unit

){

    Scaffold(
        content = {
            HomeContent(
                data = data,
                filtered = filtered,
                name = name,
                objectId = objectId,
                onNameChanged = onNameChanged,
                onObjectIdChanged = onObjectIdChanged,
                onInsertClicked = onInsertClicked,
                onUpdateClicked = onUpdateClicked,
                onDeleteClicked = onDeleteClicked,
                onFilterClicked = onFilterClicked
            )
        }
    )

}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    data: List<Person>,
    filtered: Boolean,
    name: String,
    objectId: String,
    onNameChanged: (String) -> Unit,
    onObjectIdChanged: (String) -> Unit,
    onInsertClicked: () -> Unit,
    onUpdateClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onFilterClicked: () -> Unit
) {
    Column(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .padding(all = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            Row {
                TextField(
                    modifier = androidx.compose.ui.Modifier.weight(1f),
                    value = objectId,
                    onValueChange = onObjectIdChanged,
                    placeholder = { Text(text = "Object ID") }
                )
                Spacer(modifier = androidx.compose.ui.Modifier.width(12.dp))
                TextField(
                    modifier = androidx.compose.ui.Modifier.weight(1f),
                    value = name,
                    onValueChange = onNameChanged,
                    placeholder = { Text(text = "Name") }
                )
            }
            Spacer(modifier = androidx.compose.ui.Modifier.height(24.dp))
            Row(
                modifier = androidx.compose.ui.Modifier
                    .fillMaxWidth()
                    .horizontalScroll(state = rememberScrollState()),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onInsertClicked) {
                    Text(text = "Add")
                }
                Spacer(modifier = androidx.compose.ui.Modifier.width(6.dp))
                Button(onClick = onUpdateClicked) {
                    Text(text = "Update")
                }
                Spacer(modifier = androidx.compose.ui.Modifier.width(6.dp))
                Button(onClick = onDeleteClicked) {
                    Text(text = "Delete")
                }
                Spacer(modifier = androidx.compose.ui.Modifier.width(6.dp))
                Button(onClick = onFilterClicked) {
                    Text(text = if (filtered) "Clear" else "Filter")
                }
            }
        }
        Spacer(modifier = androidx.compose.ui.Modifier.height(24.dp))
        LazyColumn(modifier = androidx.compose.ui.Modifier.weight(1f)) {
            items(items = data, key = { it._id.toHexString() }) {
                PersonView(
                    id = it._id.toHexString(),
                    name = it.name,
                    timestamp = it.timestamp
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PersonView(id: String, name: String, timestamp: RealmInstant) {
    Row(modifier = androidx.compose.ui.Modifier.padding(bottom = 24.dp)) {
        Column(modifier = androidx.compose.ui.Modifier.weight(1f)) {
            Text(
                text = name,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold
                )
            )
            SelectionContainer {
                Text(
                    text = id,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    .format(Date.from(timestamp.toInstant())).uppercase(),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun RealmInstant.toInstant(): Instant {
    val sec: Long = this.epochSeconds
    val nano: Int = this.nanosecondsOfSecond
    return if (sec >= 0) {
        Instant.ofEpochSecond(sec, nano.toLong())
    } else {
        Instant.ofEpochSecond(sec - 1, 1_000_000 + nano.toLong())
    }
}
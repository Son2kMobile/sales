package com.ngoctuan.sales

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ngoctuan.sales.Adapter.PeopleAdapter
import com.ngoctuan.sales.databinding.ActivityMainBinding
import com.ngoctuan.sales.db.Person
import com.ngoctuan.sales.db.PersonViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.openxml4j.exceptions.InvalidFormatException
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.usermodel.CellType.*
import java.io.IOException
import kotlin.coroutines.CoroutineContext
import kotlin.math.ceil


class HE : AppCompatActivity() {
    private lateinit var dialIntent: Intent

    // The class to detect swipes and drags
    private lateinit var itemTouchHelper: ItemTouchHelper
    private val personAdapter by lazy {
        PeopleAdapter { position ->
            Log.d("SSS", ": ${listPeople[position].number}")

            dialIntent =
                Intent(Intent.ACTION_CALL) // ACTION_DIAL để hiển thị số điện thoại trong giao diện điện thoại, ACTION_CALL để thực hiện cuộc gọi tự động.
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    101
                )
            } else {
                try {
                    Toast.makeText(this, "ahhaa", Toast.LENGTH_SHORT).show()
//                    object : SwipeHelper(this, activityBinding.rvList, false) {
//                        override fun instantiateUnderlayButton(
//                            viewHolder: RecyclerView.ViewHolder?,
//                            underlayButtons: MutableList<UnderlayButton>?
//                        ) {
//                            listPeople[position].number?.let {
//                                MainScope().launch(Dispatchers.Default) {
//                                    personViewModel.checkExistPhone(it).collect {
//                                        if (it.number?.isBlank() == true && it.phoneParent?.isBlank() == true) {
//                                            return@collect
//                                        }
//                                        if (it.number?.isNotBlank() == true && it.phoneParent?.isNotBlank() == true) {
//                                            underlayButtons?.addAll(
//                                                arrayOf(
//                                                    UnderlayButton(
//                                                        "Call student",
//                                                        AppCompatResources.getDrawable(
//                                                            this@HE,
//                                                            R.drawable.ic_call
//                                                        ),
//                                                        R.color.green,
//                                                        R.color.white
//                                                    ) {
//                                                        dialIntent.data =
//                                                            Uri.parse("tel:${listPeople[position].number}")
//                                                        openActivityCallPhone(dialIntent)
//                                                    },
//
//                                                    UnderlayButton(
//                                                        "Call parent",
//                                                        AppCompatResources.getDrawable(
//                                                            this@HE,
//                                                            R.drawable.ic_call
//                                                        ),
//                                                        R.color.green,
//                                                        R.color.white
//                                                    ) {
//                                                        dialIntent.data =
//                                                            Uri.parse("tel:${listPeople[position].phoneParent}")
//                                                        openActivityCallPhone(dialIntent)
//                                                    },
//                                                )
//                                            )
//                                        } else if (it.number?.isNotBlank() == true && it.phoneParent?.isBlank() == true) {
//                                            underlayButtons?.add(
//                                                UnderlayButton(
//                                                    "Call student",
//                                                    AppCompatResources.getDrawable(
//                                                        this@HE,
//                                                        R.drawable.ic_call
//                                                    ),
//                                                    R.color.green,
//                                                    R.color.white
//                                                ) {
//                                                    dialIntent.data =
//                                                        Uri.parse("tel:${listPeople[position].number}")
//                                                    openActivityCallPhone(dialIntent)
//                                                }
//                                            )
//                                        } else if (it.number?.isBlank() == true && it.phoneParent?.isNotBlank() == true) {
//                                            UnderlayButton(
//                                                "Call parent",
//                                                AppCompatResources.getDrawable(
//                                                    this@HE,
//                                                    R.drawable.ic_call
//                                                ),
//                                                R.color.green,
//                                                R.color.white
//                                            ) {
//                                                dialIntent.data =
//                                                    Uri.parse("tel:${listPeople[position].phoneParent}")
//                                                openActivityCallPhone(dialIntent)
//                                            }
//                                        }
//                                    }
//                                }
//
//                            }
//
//                        }
//
//                    }
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            }


        }
    }

    private fun openActivityCallPhone(dialIntent: Intent) {
        startActivity(dialIntent)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                openActivityCallPhone(dialIntent)
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE),
                101
            )
            Toast.makeText(this, "Please open permission", Toast.LENGTH_SHORT).show()
        }
    }

    private lateinit var listPeople: List<Person>
    private lateinit var person: Person
    private val FILE_SELECT_CODE: Int = 100

    //    private lateinit var toolbar: ActionBar
    private val personViewModel: PersonViewModel by lazy {
        ViewModelProvider(
            this,
            PersonViewModel.AccountViewModelFactory(this.application)
        )[PersonViewModel::class.java]
    }
    private lateinit var activityBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        supportActionBar?.title = "Test title"
        supportActionBar?.setIcon(R.drawable.ic_search)
        activityBinding.rvList.setHasFixedSize(true)
        activityBinding.rvList.layoutManager = LinearLayoutManager(this)

        val pickFileButton: Button = findViewById(R.id.pickFileButton)
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(activityBinding.rvList)

        pickFileButton.setOnClickListener {
            personViewModel.deleteAllPeople()
            importData()
        }

        CoroutineScope(Dispatchers.Default).launch {
            withContext(Dispatchers.Main) {
                personViewModel.getAllPeople().collect {
                    personAdapter.setAcc(it)
                    activityBinding.rvList.adapter = personAdapter
                }
            }
        }

        addMenuProvider(object : MenuProvider, SearchView.OnQueryTextListener {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.item_menu, menu)
//                val search = menu.findItem(R.id.actionSearch).actionView as SearchView
//                search.isSubmitButtonEnabled = false
//                search.setOnQueryTextListener(this)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = true

            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    val query = "%$newText%"
                    personViewModel.searchNote(query)
                }
                return false
            }

        }, this)

    }

    val simpleItemTouchCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (direction == ItemTouchHelper.LEFT) {
                    personAdapter.notifyItemChanged(position)
                    Toast.makeText(baseContext, "Swipe left", Toast.LENGTH_SHORT).show()
                } else if (direction == ItemTouchHelper.RIGHT) {
                    personAdapter.notifyItemChanged(position)
                    Toast.makeText(baseContext, "Swipe right", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                    val p = Paint()
                    val width = height / 3

                    if (dX < 0) {
                        p.color = baseContext.getColor(R.color.green)
                        val background = RectF(
                            itemView.right.toFloat() + dX,
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat()
                        )
                        c.drawRect(background, p)
                        val icon =
                            BitmapFactory.decodeResource(baseContext.resources, R.drawable.ic_call)
                        if (icon != null && !icon.isRecycled) {
                            val margin = (dX / 5 - width) / 2
                            val iconDest = RectF(
                                itemView.right.toFloat() + margin,
                                itemView.top.toFloat() + width,
                                itemView.right.toFloat() + (margin + width),
                                itemView.bottom.toFloat() - width
                            )
                            c.drawBitmap(icon, null, iconDest, p)
                        }
                    }
                    if (dX > 0) {
                        p.color = baseContext.getColor(R.color.green)
                        val icon = BitmapFactory.decodeResource(resources, R.drawable.ic_call)
                        if (icon != null && !icon.isRecycled) {
                            val margin = (dX / 5 - width) / 2
                            val iconDest = RectF(
                                margin,
                                itemView.top.toFloat() + width,
                                margin + width,
                                itemView.bottom.toFloat() - width
                            )
                            c.drawBitmap(icon, null, iconDest, p)
                        }
                    }
                }
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX / 5,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }


    private fun importData() {
        // Start the file picker activity
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        startActivityForResult(intent, FILE_SELECT_CODE)
    }

    private fun readExcelFile(uri: Uri) {
        try {
            activityBinding.tvFile.setText(getFileNameFromUri(uri))
            val inputStream = contentResolver.openInputStream(uri)
            val workbook = WorkbookFactory.create(inputStream)
            val sheetCount = workbook.numberOfSheets

            //Get the number of worksheets
            for (i in 0 until sheetCount) {
                Log.d("SSS", "\nsheet: ${workbook.getSheetName(i)}")
                // Bắt đầu từ dòng thứ hai
                val sheetName = workbook.getSheetName(i)
                val rowIterator: Iterator<Row> =
                    workbook.getSheet(sheetName).iterator()
                var rowNum = 0
                var fullName = ""
                var phone = ""
                var phoneParent = ""
                var description = ""
                var note = ""
                while (rowIterator.hasNext()) {

                    val row = rowIterator.next()
                    if (rowNum > 0) { // Bỏ qua dòng đầu tiên
                        fullName = ""
                        phone = ""
                        phoneParent = ""
                        description = ""
                        note = ""
                        val cellIterator = row.cellIterator()
                        while (cellIterator.hasNext()) {
                            val cell = cellIterator.next()
                            when (cell?.cellType) {
                                STRING -> {
                                    when (cell.columnIndex) {
                                        1 -> {
                                            fullName = cell.stringCellValue
                                        }

                                        4 -> {
                                            phone = cell.stringCellValue
                                        }

                                        6 -> {
                                            phoneParent = cell.stringCellValue
                                        }

                                        8 -> {
                                            description = cell.stringCellValue
                                        }

                                        9 -> {
                                            note = cell.stringCellValue
                                        }
                                    }

//                                    val phoneParent = cellValue[5].toString()
//                                    Log.d("SSS", "phoneParent: $phoneParent ")
//


                                }

                                NUMERIC -> {
                                    val cellValue = cell.numericCellValue
                                    Log.d("SSS", "NUMERIC: $cellValue")
                                }

                                _NONE -> {

                                }

                                FORMULA -> {

                                }

                                BLANK -> {
                                    continue
                                }

                                BOOLEAN -> {}
                                ERROR -> {}
                                null -> {
                                }
                            }

                        }

                    }
                    person = Person(
                        0,
                        fullName,
                        phone,
                        false,
                        i,
                        sheetName,
                        description,
                        note,
                        phoneParent
                    )
                    if (fullName != "") {
                        personViewModel.insertPerson(person)
                    }

                    rowNum++
                }
            }
            workbook.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InvalidFormatException) {
            e.printStackTrace()
        }
    }

    private fun readExcel(uri: Uri) {
        try {
            activityBinding.tvFile.setText(getFileNameFromUri(uri))
            val inputStream = contentResolver.openInputStream(uri)
            val wb = HSSFWorkbook(inputStream)
            for (i in 0 until wb.numberOfSheets) {
                val sheet = wb.getSheet(wb.getSheetName(i))
                val formulaEvaluator = wb.creationHelper.createFormulaEvaluator()
                sheet.forEach { row ->
                    row.forEach { ceil ->
                        when (formulaEvaluator?.evaluateFormulaCell(ceil)) {
                            _NONE -> {

                            }

                            NUMERIC -> {
                                Log.d("SSS", "STRING: ${ceil.numericCellValue}")

                            }

                            STRING -> {
                                val cellValue = ceil.stringCellValue
                                Log.d("SSS", "STRING: $cellValue")
                            }

                            FORMULA -> {
                            }

                            BLANK -> {
                            }

                            BOOLEAN -> {

                            }

                            null -> return
                            ERROR -> {

                            }
                        }

                    }
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InvalidFormatException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK) {
            val uri = data?.data
            uri?.let {
                CoroutineScope(Dispatchers.Default).launch {
                    readExcelFile(it)
                    withContext(Dispatchers.Main) {
                        personViewModel.getAllPeople().collect {
                            personAdapter.setAcc(it)
                            listPeople = it
                        }
                    }
                }
                activityBinding.rvList.adapter = personAdapter


            }
        }
    }

    private fun getFileNameFromUri(uri: Uri): String? {
        var fileName: String? = null
        val cursor = this.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                fileName = it.getString(displayNameIndex)
            }
        }
        return fileName
    }
}


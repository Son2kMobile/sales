//package com.ngoctuan.sales
//
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.provider.OpenableColumns
//import android.util.Log
//import android.widget.Button
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.ngoctuan.sales.Adapter.PeopleAdapter
//import com.ngoctuan.sales.databinding.ActivityMainBinding
//import com.ngoctuan.sales.db.PersonViewModel
//import org.apache.poi.openxml4j.exceptions.InvalidFormatException
//import org.apache.poi.ss.usermodel.*
//import java.io.IOException
//
//
//class MainActivity : AppCompatActivity() {
//    private val FILE_SELECT_CODE: Int = 100
//    private val personViewModel: PersonViewModel by lazy {
//        ViewModelProvider(
//            this,
//            PersonViewModel.AccountViewModelFactory(this.application)
//        )[PersonViewModel::class.java]
//    }
//    private lateinit var activityBinding: ActivityMainBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        activityBinding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(activityBinding.root)
//
//        val adapter = PeopleAdapter(this)
//        activityBinding.rvList.setHasFixedSize(true)
//        activityBinding.rvList.layoutManager = LinearLayoutManager(this)
//        adapter.setAcc(personViewModel.getAllPeople())
//        activityBinding.rvList.adapter = adapter
//
//        val pickFileButton: Button = findViewById(R.id.pickFileButton)
//
//        pickFileButton.setOnClickListener {
//            personViewModel.deleteAllPeople()
//            importData()
//        }
//
//    }
//
//    private fun importData() {
//        // Start the file picker activity
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        intent.type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
//        startActivityForResult(intent, FILE_SELECT_CODE)
//    }
//
//    private fun readExcelFile(uri: Uri) {
//        try {
//            activityBinding.tvFile.setText(getFileNameFromUri(uri))
//            val inputStream = contentResolver.openInputStream(uri)
//            val workbook = WorkbookFactory.create(inputStream)
//            val sheetCount = workbook.numberOfSheets
//            //Get the number of worksheets
//            for (i in 0 until sheetCount) {
//                Log.d("Cell Value", "sheet: ${workbook.getSheetName(i)}")
//                val sheet = workbook.getSheetAt(0)
////                workbook.getSheet(workbook.getSheetName(i))
//                // Bắt đầu từ dòng thứ hai
//                val rowIterator: Iterator<Row> = workbook.getSheet(workbook.getSheetName(i)).iterator()
//                var rowNum = 0
//                while (rowIterator.hasNext()) {
//                    val row = rowIterator.next()
//                    if (rowNum > 0) { // Bỏ qua dòng đầu tiên
//                        val cellIterator = row.cellIterator()
//
//                        var name: String? = null
//                        var number: String? = null
//                        // Lấy chỉ 2 ô đầu tiên từ mỗi dòng
//                        for (i in 0 until 2) {
//                            if (cellIterator.hasNext()) {
//                                val cell = cellIterator.next()
//                                Log.d("Cell Value", "cell: $cell")
//                                when (cell.cellType) {
//                                    CellType.STRING -> {
//                                        Log.d("Cell Value", "cellType ${cell.cellType}")
//
//                                        val cellValue = cell.stringCellValue
//                                        if (i == 0)
//                                            name = cellValue
//                                        else if (i == 1)
//                                            number = cellValue
//                                        Log.d("Cell Value (String)", cellValue)
//                                    }
//
//                                    else -> {
//                                        Log.d("Cell Value", "${cell.cellType}")
//
//                                        name = null
//                                        number = null
//                                        Log.d("Cell Value", "Other type")
//                                    }
//                                }
//                            }
//
//                        }
////                    if (name != null && number != null)
////                        personViewModel.insertPerson(Person(0, name, number, false))
//                    }
//                    rowNum++
//                }
//                workbook.close()
//            }
//
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } catch (e: InvalidFormatException) {
//            e.printStackTrace()
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK) {
//            val uri = data?.data
//            uri?.let {
//                readExcelFile(it)
//            }
//        }
//    }
//
//    fun getFileNameFromUri(uri: Uri): String? {
//        var fileName: String? = null
//        val cursor = this.contentResolver.query(uri, null, null, null, null)
//        cursor?.use {
//            if (it.moveToFirst()) {
//                val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//                fileName = it.getString(displayNameIndex)
//            }
//        }
//        return fileName
//    }
//}
//

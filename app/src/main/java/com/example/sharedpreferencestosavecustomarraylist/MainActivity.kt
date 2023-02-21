package com.example.sharedpreferencestosavecustomarraylist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private val FILE_NAME = "MyList"

    // Create an array to store the list of selected checkboxes
    private val vehicleList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun checkBoxClickButton(view: View) {
        // Add selected checkboxes to vehicleList, also remove the unselected from the list
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked
            when (view.id) {
                R.id.ch_car -> if (checked) vehicleList.add("Car") else vehicleList.remove("Car")
                R.id.ch_truck -> if (checked) vehicleList.add("Truck") else vehicleList.remove("Truck")
                R.id.ch_bus -> if (checked)  vehicleList.add("Bus") else vehicleList.remove("Bus")
                R.id.ch_boat -> if (checked) vehicleList.add("Boat") else vehicleList.remove("Boat")
                R.id.ch_airplane -> if (checked)  vehicleList.add("Airplane") else vehicleList.remove("Airplane")
                R.id.ch_bicycle -> if (checked)  vehicleList.add("Bicycle") else vehicleList.remove("Bicycle")
            }
        }

        // Just to see how many options have been selected
        Log.d(TAG, "$vehicleList")
    }

    private fun clearCheckBoxes(){
        // Clear the checkboxes, put all the checkboxes in a list so that their isChecked attribute
        // is set to False, used a for loop instead of manually clearing each
        val checkboxList = listOf(R.id.ch_car, R.id.ch_truck, R.id.ch_bus, R.id.ch_boat, R.id.ch_airplane, R.id.ch_bicycle)
        for (element in checkboxList){
            findViewById<CheckBox>(element).isChecked = false
        }
    }

    private fun loadCheckBoxOptions(vehicle: String) {
        // if the passed item matches one of the value, then select corresponding checkbox
        val checkBoxId = when (vehicle) {
            "Car"       -> R.id.ch_car
            "Truck"     -> R.id.ch_truck
            "Bus"       -> R.id.ch_bus
            "Boat"      -> R.id.ch_boat
            "Airplane"  -> R.id.ch_airplane
            else -> R.id.ch_bicycle
        }
        findViewById<CheckBox>(checkBoxId).isChecked = true
    }

    // A button to save the data to SharedPreferences
    fun saveButton(view: View) {

        if (vehicleList.size == 0) { // Make sure the list has some items
            Toast.makeText(this, "Nothing to save", Toast.LENGTH_SHORT).show()
            return
        }
        // Create an instance of getSharedPreferences for edit
        val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Create an instance of Gson (make sure to include its dependency first to be able use gson)
        val gson = Gson()
        // toJson() method serializes the specified object into its equivalent Json representation.
        val vehicleListJson = gson.toJson(vehicleList)
        // Put the  Json representation, which is a string, into sharedPreferences
        editor.putString("vehicles", vehicleListJson)
        // Apply the changes
        editor.apply()

        val toast = Toast.makeText(this, "Saved", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP, 0, 150)
        toast.show()


    }

    // A button to load the previously saved value
    fun loadButton(view: View) {

        // Create an instance of getSharedPreferences for retrieve the data
        val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        // Retrieve data using the key, default value is empty string in case no saved data in there
        val tasks = sharedPreferences.getString("vehicles", "") ?: ""

        if (tasks.isNotEmpty()){

            // Create an instance of Gson
            val gson = Gson()
            // create an object expression that descends from TypeToken
            // and then get the Java Type from that
            val sType = object : TypeToken<List<String>>() { }.type
            // provide the type specified above to fromJson() method
            // this will deserialize the previously saved Json into an object of the specified type (e.g., list)
            val savedVehicleList = gson.fromJson<List<String>>(tasks, sType)

            // Clear the list and checkboxes in case there are some items
            vehicleList.clear()
            clearCheckBoxes()

            // Iterate each item in the list and select the corresponding checkbox
            for (vehicle in savedVehicleList) {
                loadCheckBoxOptions(vehicle)
                vehicleList.add(vehicle)
                Log.d(TAG, vehicle)
            }
        }
    }

    // A button to clear the previously saved value
    fun clearButton(view: View) {
        // Create an instance of getSharedPreferences for edit i.e., delete
        val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        clearCheckBoxes()
        vehicleList.clear()
        Log.d(TAG, "Clearing saved data...")
    }
}
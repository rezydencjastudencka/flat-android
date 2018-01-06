package pl.rpieja.flat

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView

import pl.rpieja.flat.dto.User
import pl.rpieja.flat.viewmodels.NewChargeViewModel

class NewChargeActivity : AppCompatActivity() {
    object Constants {
        const val SET_DATE_TAG = "pl.rpieja.flat.newCharge.setDate"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_charge)

        val toolbar: Toolbar = findViewById(R.id.toolbar_1)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val newChargeViewModel = ViewModelProviders.of(this)
                .get(NewChargeViewModel::class.java)
        newChargeViewModel.loadUsers(applicationContext)

        prepareDateSelectionField(newChargeViewModel)

        bindEditTextWithLiveData(findViewById(R.id.new_charge_name), newChargeViewModel.chargeName)

        bindEditTextWithLiveData(findViewById(R.id.newChargeAmount), newChargeViewModel.chargeAmount)

        val users: RecyclerView = findViewById(R.id.newChargeUsersList)
        users.layoutManager = LinearLayoutManager(this)
        users.adapter = UsersListAdapter(newChargeViewModel.users,
                newChargeViewModel.selectedUsers, this)

        val accept: FloatingActionButton = findViewById(R.id.accept_button)
        accept.isEnabled = newChargeViewModel.isValid.value ?: false
        newChargeViewModel.isValid.observe(this, Observer { isValid ->
            accept.isEnabled = isValid ?: false
            if (isValid == true) {
                accept.backgroundTintList = ColorStateList.valueOf(
                        resources.getColor(R.color.colorAccent, theme))
            } else {
                accept.backgroundTintList = ColorStateList.valueOf(
                        resources.getColor(R.color.iconColorGreyDark, theme))
            }
        })
        accept.setOnClickListener({
            newChargeViewModel.createCharge(
                    this@NewChargeActivity, Runnable (this@NewChargeActivity::finish))
        })
    }

    private fun bindEditTextWithLiveData(field: EditText, liveData: MutableLiveData<String>) {
        val value = liveData.value
        if (value != null) {
            field.setText(value)
        }
        field.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                liveData.value = editable.toString()
            }

            override fun beforeTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })
    }

    private fun prepareDateSelectionField(newChargeViewModel: NewChargeViewModel) {
        val currentSetDate = fragmentManager.findFragmentByTag(Constants.SET_DATE_TAG) as DateDialog?
        currentSetDate?.setDateSetListener(newChargeViewModel.chargeDate::setValue)


        val newChargeDate: TextView = findViewById(R.id.newChargeDate)
        newChargeDate.setOnClickListener({
            val dialog = DateDialog()
            dialog.setDateSetListener(newChargeViewModel.chargeDate::setValue)
            val ft = fragmentManager.beginTransaction()
            dialog.show(ft, Constants.SET_DATE_TAG)
        })


        newChargeViewModel.chargeDate.observe(this, Observer { calendar ->
            newChargeDate.text = DateFormat.getLongDateFormat(this).format(calendar!!.time)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_new_charge, menu)
        return true
    }
}

private class ViewHolder(val mCheckBox: CheckBox) : RecyclerView.ViewHolder(mCheckBox)

private class UsersListAdapter(
        private val users: MutableLiveData<List<User>>,
        private val selectedUsers: MutableLiveData<Set<User>>,
        lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<ViewHolder>() {

    init {
        users.observe(lifecycleOwner, Observer { this.notifyDataSetChanged() })
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val checkbox = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.user_list_item, parent, false) as CheckBox

        return ViewHolder(checkbox)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val user = users.value!![position]
        holder!!.mCheckBox.text = user.name
        holder.mCheckBox.isChecked = selectedUsers.value?.contains(user) ?: false
        holder.mCheckBox.setOnClickListener {
            val newUsers = if (selectedUsers.value?.contains(user) == true) {
                selectedUsers.value?.minus(user) ?: emptySet()
            } else {
                (selectedUsers.value ?: emptySet()).plus(user)
            }
            selectedUsers.setValue(newUsers)
        }
    }

    override fun getItemCount(): Int {
        return users.value?.size ?: 0
    }
}


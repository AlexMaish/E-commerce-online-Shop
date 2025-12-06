package com.example.onlineshop.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onlineshop.model.CategoryModel
import com.example.onlineshop.model.ItemsModel
import com.example.onlineshop.model.SliderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class MainViewModel : ViewModel() {

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val _category = MutableLiveData<MutableList<CategoryModel>>()
    private val _banner = MutableLiveData<List<SliderModel>>()
    private val _recommended = MutableLiveData<MutableList<ItemsModel>>()
    private val _filteredItems = MutableLiveData<MutableList<ItemsModel>>()

    val banners: LiveData<List<SliderModel>> = _banner
    val categories: LiveData<MutableList<CategoryModel>> = _category
    val recommended: LiveData<MutableList<ItemsModel>> = _recommended
    val filteredItems: LiveData<MutableList<ItemsModel>> = _filteredItems

    fun loadFiltered(id: String) {
        val ref = firebaseDatabase.getReference("Items")
        val query: Query = ref.orderByChild("categoryId").equalTo(id)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ItemsModel>()

                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(ItemsModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                _filteredItems.value = lists // Update filteredItems instead of recommended
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error appropriately
            }
        })
    }

    fun loadRecommended() {
        val ref = firebaseDatabase.getReference("Items")
        val query: Query = ref.orderByChild("showRecommended").equalTo(true)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ItemsModel>()

                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(ItemsModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                _recommended.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error appropriately
            }
        })
    }

    fun loadBanner() {
        val ref = firebaseDatabase.getReference("Banner")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SliderModel>()

                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(SliderModel::class.java)

                    if (list != null) {
                        lists.add(list)
                    }
                }
                _banner.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error appropriately
            }
        })
    }

    fun loadCategory() {
        val ref = firebaseDatabase.getReference("Category")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<CategoryModel>()
                for (childSnapshot in snapshot.children) {
                    val category = childSnapshot.getValue(CategoryModel::class.java)
                    if (category != null) {
                        lists.add(category)
                    }
                }
                _category.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error appropriately
            }
        })
    }
}
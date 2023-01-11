package uz.gita.fooddelivery.data.repository.food

import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import uz.gita.fooddelivery.BuildConfig.*
import uz.gita.fooddelivery.data.local.MySharedPreferences
import uz.gita.fooddelivery.data.remote.request.CartDataRequest
import uz.gita.fooddelivery.data.remote.response.AdDataResponse
import uz.gita.fooddelivery.data.remote.response.CartDataResponse
import uz.gita.fooddelivery.data.remote.response.FoodDataResponse
import javax.inject.Inject

class FoodRepositoryImpl
@Inject constructor(
    private val preferences: MySharedPreferences,
    private val store: FirebaseFirestore
) : FoodRepository {

    init {
        Timber.tag("AUTH").d("PHONE: ${preferences.currentUserPhone})")
        Timber.tag("AUTH").d("UID: ${preferences.currentUID})")
        Timber.tag("AUTH").d("REMEMBER: ${preferences.isRememberUser})")
    }

    override suspend fun adsData(
        success: (List<AdDataResponse>) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        store
            .collection(ADS)
            .get()
            .addOnSuccessListener { querySnapshot ->
                Timber.d("adsData(): addOnSuccessListener isEmpty? = ${querySnapshot.isEmpty}")
                val response = querySnapshot.map { queryDocumentSnapshot ->
                    queryDocumentSnapshot.toObject(AdDataResponse::class.java)
                }
                Timber.d("adsData response size: ${response.size}")
                success.invoke(response)
            }
            .addOnFailureListener { failure.invoke(it) }
    }

    override suspend fun foodsData(
        success: (List<FoodDataResponse>) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        store.collection(FOODS)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val response = querySnapshot.map { queryDocumentSnapshot ->
                    queryDocumentSnapshot.toObject(FoodDataResponse::class.java)
                }
                success.invoke(response)
            }
            .addOnFailureListener { failure.invoke(it) }
    }

    override suspend fun foodsDataByQuery(
        query: String,
        success: (List<FoodDataResponse>) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        store
            .collection(FOODS)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val response = querySnapshot.map { documentSnapshot ->
                    documentSnapshot.toObject(FoodDataResponse::class.java)
                }
                success.invoke(response)
            }
            .addOnFailureListener { failure.invoke(it) }
    }

    override suspend fun cartData(
        success: (List<CartDataResponse>) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        store
            .collection(CART_FOODS)
            .whereEqualTo(UID, preferences.currentUID)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val response = querySnapshot.map { queryDocumentSnapshot ->
                    queryDocumentSnapshot.toObject(CartDataResponse::class.java)
                }
                success.invoke(response)
            }
            .addOnFailureListener {
                failure.invoke(it)
            }
    }

    override fun addFoodToCart(
        name: String,
        price: Int,
        image: String,
        success: () -> Unit,
        failure: (Throwable) -> Unit
    ) {
        val currentTime = System.currentTimeMillis()
        Timber.d("addFoodToCart: $currentTime")
        store
            .collection(CART_FOODS)
            .document(currentTime.toString())
            .set(
                CartDataRequest(
                    id = currentTime,
                    uid = preferences.currentUID,
                    image = image,
                    name = name,
                    price = price,
                )
            )
            .addOnSuccessListener {
                success.invoke()
                Timber.d("addFoodToCart: success")
            }
            .addOnFailureListener {
                failure.invoke(it)
                Timber.d("addFoodToCart: failure = ${it.message}")
            }
    }

    override suspend fun deleteFoodFromCart(
        deletedItemId: Long,
        success: () -> Unit,
        failure: (Throwable) -> Unit
    ) {
        store
            .collection(CART_FOODS)
            .document(deletedItemId.toString())
            .delete()
            .addOnSuccessListener { success.invoke() }
            .addOnFailureListener { failure.invoke(it) }
    }

    override suspend fun updateFoodPiecesCart(
        id: Long,
        pieces: Int,
        success: () -> Unit,
        failure: (Throwable) -> Unit
    ) {
        store
            .collection(CART_FOODS)
            .document(id.toString())
            .update(CART_FOODS_PIECES, pieces)
            .addOnSuccessListener { success.invoke() }
            .addOnFailureListener { failure.invoke(it) }
    }

    override suspend fun placeOrder(
        success: () -> Unit,
        failure: (Throwable) -> Unit
    ) {
        store
            .collection(CART_FOODS)
            .whereEqualTo(UID, preferences.currentUID)
            .get()
            .addOnCompleteListener { task ->
                val documentId = task.result.map { it.id }
                val temp = store.collection(CART_FOODS)
                documentId.forEach { temp.document(it).update(CART_FOODS_IS_CHECKOUT, true) }
                success.invoke()
            }
            .addOnFailureListener { failure.invoke(it) }
    }

    override suspend fun historyData(
        success: (List<CartDataResponse>) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        store
            .collection(CART_FOODS)
            .whereEqualTo(UID, preferences.currentUID)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val response = querySnapshot.map { it.toObject(CartDataResponse::class.java) }
                success.invoke(response)
            }
            .addOnFailureListener { failure.invoke(it) }
    }

}

/*
val list = ArrayList<FoodsGroupData>()
        val map = HashMap<CategoryData, ArrayList<FoodData>>()
        store.collection("test_foods")
            .get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.documents.map { documentSnapshot ->
                    var category: CategoryData? = null
                    documentSnapshot.getDocumentReference("category")
                        ?.addSnapshotListener { value, error ->
                            val data = value?.toObject(CategoryData::class.java)
                            category = data
                            map[data!!] = ArrayList()
//                            temp.category = data
//                            Timber.tag("AAA").d("data: $data")
                        }
                    val foods = documentSnapshot.get(FOODS) as List<DocumentReference>
                    foods.forEach { documentReference ->
                        documentReference.get().addOnSuccessListener {
                            val data = it.toObject(FoodData::class.java)
                            if (data != null) { map[category]?.add(data) }
//                            Timber.tag("AAA").d("data: $data")
                        }
                    }
//                    temp.foodList = foodList
//                    list.add(temp)
                }
            }
* */

/*store.collection("test_foods")
            .get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.documents.map { documentSnapshot ->
                    documentSnapshot.getDocumentReference("category")?.addSnapshotListener { value, error ->
                            val data = value?.toObject(CategoryData::class.java)
                            Timber.tag("LLLL").d("data category: ${data?.id}")
                        }
                    val foods = documentSnapshot.get(FOODS) as List<DocumentReference>
                    foods.forEach { documentReference ->
                        documentReference.get().addOnSuccessListener {
                            val data = it.toObject(FoodData::class.java)
                            Timber.tag("LLLL").d("data food: ${data?.id}")
                        }
                    }
//                    temp.foodList = foodList
//                    list.add(temp)
                }
            }*/
/*store.collection("test_foods_new").get().addOnSuccessListener { querySnapshot ->
    querySnapshot.documents.map { documentSnapshot ->
        val foodsGroupData =
            documentSnapshot.getDocumentReference("category") as List<DocumentReference>
        val categoryData = foodsGroupData[0].addSnapshotListener { value, error ->

        }
        val foodsList = ArrayList<FoodData>()
        for (i in 1 until foodsGroupData.size) {
            val data = foodsGroupData[i].addSnapshotListener { value, error ->
                value?.toObject(FoodData::class.java)
                foodsList.add(data)
            }

        }
    }
}*/

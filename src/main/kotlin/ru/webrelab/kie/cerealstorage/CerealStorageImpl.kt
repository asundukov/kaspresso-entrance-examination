package ru.webrelab.kie.cerealstorage

import kotlin.math.min

class CerealStorageImpl(
    override val containerCapacity: Float,
    override val storageCapacity: Float
) : CerealStorage {

    private val storage = mutableMapOf<Cereal, Float>()
    private val maxContainersCount = storageCapacity / containerCapacity

    /**
     * Блок инициализации класса.
     * Выполняется сразу при создании объекта
     */
    init {
        require(containerCapacity >= 0) {
            "Ёмкость контейнера не может быть отрицательной"
        }
        require(storageCapacity >= containerCapacity) {
            "Ёмкость хранилища не должна быть меньше ёмкости одного контейнера"
        }
    }

    override fun addCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0) {
            "Количество добавляемой крупы не должно быть отрицательным"
        }
        if (!storage.containsKey(cereal)) {
            if (storage.size >= maxContainersCount) {
                throw IllegalStateException("Хранилище не позволяет разместить ещё один контейнер для новой крупы")
            }
            storage[cereal] = 0f
        }
        val availableSpace = containerCapacity - storage[cereal]!!
        val amountToAdd = min(availableSpace, amount)
        storage[cereal] = storage[cereal]!! + amountToAdd
        return amount - amountToAdd
    }

    override fun getCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0) {
            "Количество забираемой крупы не должно быть отрицательным"
        }
        if (!storage.containsKey(cereal)) {
            return 0f
        }
        val amountToGet = min(storage[cereal]!!, amount)
        storage[cereal] = storage[cereal]!! - amountToGet
        return amountToGet
    }

    override fun removeContainer(cereal: Cereal): Boolean = when {
        !storage.containsKey(cereal) -> true
        storage[cereal] != 0f -> false
        else -> {
            storage.remove(cereal)
            true
        }
    }

    override fun getAmount(cereal: Cereal) = storage[cereal] ?: 0f

    override fun getSpace(cereal: Cereal) = if (storage.containsKey(cereal)) {
                containerCapacity - storage[cereal]!!
            } else {
                0f
            }

    override fun toString(): String = "CerealStorageImpl(storage=$storage)"
}

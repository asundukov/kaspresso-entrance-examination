package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CerealStorageImplTest {

    private val storage = CerealStorageImpl(10f, 20f)

    @Test
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4f, 10f)
        }
    }

    @Test
    fun `should accept zero amount of cereal`() {
        val amount = storage.addCereal(Cereal.BUCKWHEAT, 0f)
        assertEquals(0f, amount)
    }

    @Test
    fun `should not accept negative amount of cereal`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.addCereal(Cereal.BUCKWHEAT, -1f)
        }
    }

    @Test
    fun `should normally add a new type the cereal`() {
        val amount = storage.addCereal(Cereal.BUCKWHEAT, 4f)
        assertEquals(0f, amount)
    }

    @Test
    fun `should normally add several new types the cereal`() {
        val amount = storage.addCereal(Cereal.BUCKWHEAT, 8f)
        assertEquals(0f, amount)

        val amount2 = storage.addCereal(Cereal.PEAS, 7f)
        assertEquals(0f, amount2)
    }

    @Test
    fun `should add multiple requests of a single type if cereal`() {
        storage.addCereal(Cereal.BUCKWHEAT, 8f)
        val amount = storage.addCereal(Cereal.BUCKWHEAT, 6f)
        assertEquals(4f, amount)
    }

    @Test
    fun `should not accept more than storage capacity`() {
        storage.addCereal(Cereal.BUCKWHEAT, 8f)
        storage.addCereal(Cereal.PEAS, 7f)
        assertThrows(IllegalStateException::class.java) {
            storage.addCereal(Cereal.RICE, 7f)
        }
    }

    @Test
    fun `should throw if get less than zero`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.getCereal(Cereal.BUCKWHEAT, -1f)
        }
    }

    @Test
    fun `should get 0 if no cereal`() {
        val amount = storage.getCereal(Cereal.BUCKWHEAT, 5f)
        assertEquals(0f, amount)
    }

    @Test
    fun `should get the requested amount of cereal`() {
        storage.addCereal(Cereal.BUCKWHEAT, 8f)
        val amount = storage.getCereal(Cereal.BUCKWHEAT, 5f)
        assertEquals(5f, amount)
    }

    @Test
    fun `should get the rest of cereal`() {
        storage.addCereal(Cereal.BUCKWHEAT, 4f)
        val amount = storage.getCereal(Cereal.BUCKWHEAT, 5f)
        assertEquals(4f, amount)
    }

    @Test
    fun `should remove container if not exists`() {
        val removed = storage.removeContainer(Cereal.BUCKWHEAT)
        assertTrue(removed)
    }

    @Test
    fun `should not remove container if not empty`() {
        storage.addCereal(Cereal.BUCKWHEAT, 4f)
        val removed = storage.removeContainer(Cereal.BUCKWHEAT)
        assertFalse(removed)
    }

    @Test
    fun `should remove container if empty`() {
        storage.addCereal(Cereal.BUCKWHEAT, 4f)
        storage.getCereal(Cereal.BUCKWHEAT, 4f)
        val removed = storage.removeContainer(Cereal.BUCKWHEAT)
        assertTrue(removed)
    }

    @Test
    fun getAmount() {
        storage.addCereal(Cereal.BUCKWHEAT, 4f)
        val amount = storage.getAmount(Cereal.BUCKWHEAT)
        assertEquals(4f, amount)
    }

    @Test
    fun `getAmount of non-existent container`() {
        val amount = storage.getAmount(Cereal.BUCKWHEAT)
        assertEquals(0f, amount)
    }

    @Test
    fun getSpace() {
        storage.addCereal(Cereal.BUCKWHEAT, 4f)
        val space = storage.getSpace(Cereal.BUCKWHEAT)
        assertEquals(6f, space)
    }

    @Test
    fun `getSpace of non-existent container`() {
        val space = storage.getSpace(Cereal.BUCKWHEAT)
        assertEquals(0f, space)
    }

    @Test
    fun `complex add and remove containers`() {
        storage.addCereal(Cereal.BUCKWHEAT, 4f)
        storage.addCereal(Cereal.PEAS, 7f)
        assertThrows(IllegalStateException::class.java) {
            storage.addCereal(Cereal.RICE, 1f)
        }
        storage.getCereal(Cereal.BUCKWHEAT, 4f)
        storage.getCereal(Cereal.PEAS, 3f)
        // true
        storage.removeContainer(Cereal.BUCKWHEAT)
        // false
        storage.removeContainer(Cereal.PEAS)
        storage.getCereal(Cereal.PEAS, 4f)
        // true
        storage.removeContainer(Cereal.PEAS)

        val amount = storage.addCereal(Cereal.RICE, 1f)
        assertEquals(0f, amount)
        val amount2 = storage.addCereal(Cereal.BULGUR, 15f)
        assertEquals(5f, amount2)
    }
}

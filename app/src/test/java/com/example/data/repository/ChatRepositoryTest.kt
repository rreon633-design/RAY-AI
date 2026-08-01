package com.example.data.repository

import com.example.data.local.ChatEntity
import com.example.data.local.MessageEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ChatRepositoryTest {

    private lateinit var fakeDao: FakeChatDao
    private lateinit var chatRepository: ChatRepository

    @Before
    fun setup() {
        fakeDao = FakeChatDao()
        chatRepository = ChatRepository(fakeDao)
    }

    @Test
    fun `createNewSession inserts session and returns id`() = runTest {
        val resultId = chatRepository.createNewSession("Test Session", "model-1", "Test Model")
        assertEquals(1L, resultId)
        assertEquals(1, fakeDao.sessions.size)
        assertEquals("Test Session", fakeDao.sessions[0].title)
    }

    @Test
    fun `deleteSession deletes messages and then session`() = runTest {
        val sessionId = chatRepository.createNewSession("S1", "m1", "M1")
        chatRepository.saveMessage(MessageEntity(chatId = sessionId, text = "H", sender = "user"))
        
        chatRepository.deleteSession(sessionId)
        
        assertEquals(0, fakeDao.sessions.size)
        assertEquals(0, fakeDao.messages.size)
    }

    @Test
    fun `saveMessage inserts message and returns id`() = runTest {
        val message = MessageEntity(chatId = 1L, text = "Hello", sender = "user")
        val resultId = chatRepository.saveMessage(message)
        assertEquals(1L, resultId)
        assertEquals(1, fakeDao.messages.size)
    }
}

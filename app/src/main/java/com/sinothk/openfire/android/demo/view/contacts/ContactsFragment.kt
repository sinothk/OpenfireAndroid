package com.sinothk.openfire.android.demo.view.contacts

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sinothk.openfire.android.xmpp.XmppConnection
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.roster.RosterEntry
import org.jivesoftware.smack.roster.RosterListener
import org.jxmpp.jid.EntityFullJid
import org.jxmpp.jid.Jid
import java.util.*


/**

 * @ author LiangYT
 * @ create 2018/10/21 2:21
 * @ Describe
 */
class ContactsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return TextView(activity)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//
////        val usm = UserSearchManager(XmppConnection.getInstance().connection)
////        val searchForm = usm.getSearchForm(XmppConnection.getInstance().connection.serviceName)
////
////        val nickname = FormField("nickname")
////        nickname.type = FormField.FORM_TYPE
////        nickname.addValue(userName)
////
////        searchForm.addField(nickname)
////
////
////        val answerForm = searchForm.createAnswerForm()
////        val data = usm.getSearchResults(answerForm,  XmppConnection.getInstance().connection.getServiceName())
////
////
////        val userList: List<RosterEntry> = XmppConnection.getInstance().allEntries
////        for (entry: RosterEntry in userList) {
////            System.out.println("name: " + entry.name)
////            System.out.println("Jid: " + entry.jid.toString())
////        }
//
//        Thread {
//            var roster: Roster = Roster.getInstanceFor(XmppConnection.getInstance().connection)
//
////            getAllEntries(roster)
//            getAllRosters(roster)
//        }.start()
//    }

//    fun getAllRosters(roster: Roster) {
//
//        val entityFullJid: EntityFullJid =XmppConnection.getInstance().connection.user
//
//
//        if (!roster.isLoaded) {
//            try {
//                roster.reloadAndWait()
//            } catch (e: SmackException.NotLoggedInException) {
//                e.printStackTrace()
//            } catch (e: SmackException.NotConnectedException) {
//                e.printStackTrace()
//            } catch (e: InterruptedException) {
//                e.printStackTrace()
//            }
//
//        }
//        // 这里获取好友后会回调到 rosterEntires 方法，具体看上一篇文章
//        roster.getEntriesAndAddListener(object : RosterListener {
//            override fun entriesAdded(collection: Collection<Jid>) {
//
//            }
//
//            override fun entriesUpdated(collection: Collection<Jid>) {
//
//            }
//
//            override fun entriesDeleted(collection: Collection<Jid>) {
//
//            }
//
//            override fun presenceChanged(presence: Presence) {
//
//            }
//        }) { collection ->
//            if (collection == null) {
//
//            }
//        }
//    }
//
//
//    @Throws(SmackException.NotLoggedInException::class, SmackException.NotConnectedException::class, InterruptedException::class)
//    private fun getAllEntries(roster: Roster): List<RosterEntry> {
//
//        val entriesList = ArrayList<RosterEntry>()
//
//        if (!roster.isLoaded)
//            roster.reloadAndWait()
//        val entries = roster.entries
//        for (entry in entries) {
//            println("Here: " + entry.name)
//            println("Here: " + entry.groups[0].entryCount)
//            println("Here: " + entry.jid.toString())
//            entriesList.add(entry)
//
//        }
//        return entriesList
//    }
}
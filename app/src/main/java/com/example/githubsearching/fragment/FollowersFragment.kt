package com.example.githubsearching.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubsearching.adapter.ListViewAdapter
import com.example.githubsearching.R
import com.example.githubsearching.UserSource
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowersFragment : Fragment() {

    private lateinit var rvGitAccount: RecyclerView
    private lateinit var progressBar: ProgressBar

    companion object{
        private val ARG_USERNAME = "username"

        fun newInstance(username: String) : FollowersFragment {
            val fragment = FollowersFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvGitAccount = view.findViewById(R.id.rv_git_followers)
        progressBar = view.findViewById(R.id.progressBarFollower)

        val username = arguments?.getString(ARG_USERNAME)

        getFollowers(username!!)
    }

    private fun getFollowers(username: String?) {
        progressBar.visibility = View.VISIBLE
        val url = "https://api.github.com/users/$username/followers"

        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "token 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")
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get(url, object: AsyncHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                var mArrayAccount= arrayListOf<UserSource>()
                try {
                    val result = String(responseBody!!)
                    val arrayData = JSONArray(result)
                    var userList = ArrayList<UserSource>()

                    for(i in 0 until arrayData.length()) {
                        val dataObject = arrayData.getJSONObject(i)
                        val name = dataObject.getString("login")
                        val type = dataObject.getString("type")
                        val avatar = dataObject.getString("avatar_url")

                        val userSource = UserSource(
                                name, type, avatar
                        )

                        userList.add(userSource)
                    }

                    mArrayAccount.addAll(userList)
                    Log.d("ArrayAccount", mArrayAccount.toString())
                    progressBar.visibility = View.INVISIBLE
                    showList(mArrayAccount)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {

            }
        })
    }

    private fun showList(mArrayAccount: ArrayList<UserSource>) {
        Log.d("ShowListGetFollowers", mArrayAccount.toString())
        rvGitAccount.layoutManager = LinearLayoutManager(activity)
        val gitUserAdapter = ListViewAdapter()
        gitUserAdapter.setData(mArrayAccount)
        rvGitAccount.adapter = gitUserAdapter

        gitUserAdapter.setOnItemClickCallback(object : ListViewAdapter.OnItemClickCallback {
            override fun onItemClicked(userSource: UserSource) {
                Toast.makeText(activity, "${userSource.name}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
package tw.chikuo.linetvrecruitment.controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_drama.*
import tw.chikuo.linetvrecruitment.R
import tw.chikuo.linetvrecruitment.model.Drama
import tw.chikuo.linetvrecruitment.model.DramaModel


class DramaActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drama)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayUseLogoEnabled(false)

        // App link
        handleIntent(intent)

        // Intent extra : drama
        if (intent.hasExtra("drama")){
            val drama = intent.getSerializableExtra("drama") as Drama?
            showDrama(drama!!)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val appLinkData = intent?.data
        if (appLinkData != null){
            val dramaId = appLinkData.lastPathSegment
            DramaModel(this@DramaActivity).getDrama(dramaId, object : DramaModel.DramaCallback{
                override fun callback(drama: Drama) {
                    showDrama(drama)
                }
            })
        }
    }

    private fun showDrama(drama: Drama) {
        Picasso.get()
                .load(drama.thumb)
                .into(imageView)
        nameTextView.text = drama.name
        ratingTextView.text = drama.getRating()
        createdAtTextView.text = getString(R.string.created_at_text, drama.getCreateTime())
        totalViewTextView.text = getString(R.string.total_view_text, drama.total_views)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

package com.yeseul.part3.chapter04

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import com.yeseul.part3.chapter04.databinding.ActivityDetailBinding
import com.yeseul.part3.chapter04.model.AppDatabase
import com.yeseul.part3.chapter04.model.Book
import com.yeseul.part3.chapter04.model.Review

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "BookSearchDB"
        ).build()

        val model = intent.getParcelableExtra<Book>("bookModel")
        binding.titleTextView.text = model?.title.orEmpty()
        binding.descriptionTextView.text = model?.description.orEmpty()
        Glide.with(binding.coverImageView.context)
            .load(model?.coverSmallUrl.orEmpty())
            .into(binding.coverImageView)

        Thread {
            val review = db.reviewDao().getOneReview(model?.id?.toInt() ?: 0)
            if (review != null) {
                runOnUiThread {
                    binding.reviewEditText.setText(review.review.orEmpty())
                }
            }


            Log.d("ERROR", "" + review)
            Log.d("ERROR", "" + model)
        }.start()

        binding.saveButton.setOnClickListener {
            Thread {
                db.reviewDao().saveReview(
                    Review(model?.id?.toInt() ?: 0, binding.reviewEditText.text.toString())
                )
            }.start()
            Toast.makeText(this, "리뷰가 저장되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
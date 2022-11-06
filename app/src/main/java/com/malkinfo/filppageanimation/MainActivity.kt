package com.malkinfo.filppageanimation

import android.graphics.*
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity



/**
 * Simple Activity for curl testing.
 *
 * @author Farida Shekh
 */
class MainActivity : AppCompatActivity() {
    private var mCurlView: CurlView? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var index = 0
        if (lastNonConfigurationInstance != null) {
            index = (lastNonConfigurationInstance as Int?)!!
        }
        mCurlView = findViewById<View>(R.id.curl) as CurlView
        mCurlView!!.setPageProvider(PageProvider())
        mCurlView!!.setSizeChangedObserver(SizeChangedObserver())
        mCurlView!!.currentIndex = index
        mCurlView!!.setBackgroundColor(-0xdfd7d0)

        // This is something somewhat experimental. Before uncommenting next
        // line, please see method comments in CurlView.
        // mCurlView.setEnableTouchPressure(true);
    }

    public override fun onPause() {
        super.onPause()
        mCurlView!!.onPause()
    }

    public override fun onResume() {
        super.onResume()
        mCurlView!!.onResume()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return mCurlView!!.currentIndex
    }



    /**
     * Bitmap provider.
     */
    private inner class PageProvider : CurlView.PageProvider {
        // Bitmap resources.
        private val mBitmapIds = intArrayOf(
            R.drawable.news, R.drawable.news3,
            R.drawable.news2, R.drawable.news1,R.drawable.news4,R.drawable.news5
        )
        override val pageCount: Int
            get() = 5

        private fun loadBitmap(width: Int,height: Int,index: Int): Bitmap {
            val b = Bitmap.createBitmap(
                width, height,
                Bitmap.Config.ARGB_8888)
            b.eraseColor(-0x1)
            val c = Canvas(b)
            val d = resources.getDrawable(mBitmapIds[index])
            val margin = 7
            val border = 3
            val r = Rect(margin, margin, width - margin, height - margin)
            var imageWidth = r.width() - border * 2
            var imageHeight = (imageWidth * d.intrinsicHeight
                    / d.intrinsicWidth)
            if (imageHeight > r.height() - border * 2) {
                imageHeight = r.height() - border * 2
                imageWidth = (imageHeight * d.intrinsicWidth
                        / d.intrinsicHeight)
            }
            r.left += (r.width() - imageWidth) / 2 - border
            r.right = r.left + imageWidth + border + border
            r.top += (r.height() - imageHeight) / 2 - border
            r.bottom = r.top + imageHeight + border + border
            val p = Paint()
            p.color = -0x3f3f40
            c.drawRect(r, p)
            r.left += border
            r.right -= border
            r.top += border
            r.bottom -= border
            d.bounds = r
            d.draw(c)
            return b
        }

        override fun updatePage(page: CurlPage?, width: Int, height: Int, index: Int) {
            when (index) {
                0 -> {
                    val front = loadBitmap(width, height, 0)
                    page!!.setTexture(front, CurlPage.SIDE_FRONT)
                    page.setColor(Color.rgb(180, 180, 180), CurlPage.SIDE_BACK)
                }
                1 -> {
                    val back = loadBitmap(width, height, 2)
                    page!!.setTexture(back, CurlPage.SIDE_BACK)
                    page.setColor(Color.rgb(127, 140, 180), CurlPage.SIDE_FRONT)
                }
                2 -> {
                    val front = loadBitmap(width, height, 1)
                    val back = loadBitmap(width, height, 3)
                    page!!.setTexture(front, CurlPage.SIDE_FRONT)
                    page.setTexture(back, CurlPage.SIDE_BACK)
                }
                3 -> {
                    val front = loadBitmap(width, height, 2)
                    val back = loadBitmap(width, height, 1)
                    page!!.setTexture(front, CurlPage.SIDE_FRONT)
                    page.setTexture(back, CurlPage.SIDE_BACK)
                    page.setColor(
                        Color.argb(127, 170, 130, 255),
                        CurlPage.SIDE_FRONT
                    )
                    page.setColor(Color.rgb(255, 190, 150), CurlPage.SIDE_BACK)
                }
                4 -> {
                    val front = loadBitmap(width, height, 0)
                    page!!.setTexture(front, CurlPage.SIDE_BOTH)
                    page.setColor(
                        Color.argb(127, 255, 255, 255),
                        CurlPage.SIDE_BACK
                    )
                }
            }
        }
    }

    /**
     * CurlView size changed observer.
     */
    private inner class SizeChangedObserver : CurlView.SizeChangedObserver {
        override fun onSizeChanged(width: Int, height: Int) {
            if (width > height) {
                mCurlView!!.setViewMode(CurlView.SHOW_TWO_PAGES)
                mCurlView!!.setMargins(.1f, .05f, .1f, .05f)
            } else {
                mCurlView!!.setViewMode(CurlView.SHOW_ONE_PAGE)
                mCurlView!!.setMargins(.1f, .1f, .1f, .1f)
            }
        }
    }
}
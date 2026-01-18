package com.livestreaming.tv.utils

import android.os.Parcelable
import android.view.KeyEvent
import android.view.View
import com.livestreaming.tv.R
import kotlinx.parcelize.Parcelize

// Fragment IDs that should show the tab bar
val fragmentsWithTabBar = setOf(
    R.id.forYouFragment,
    R.id.moviesFragment,
    R.id.liveFragment,
    R.id.tvShowsFragment,
    R.id.subscriptionFragment,
    R.id.searchScreenFragment,
    R.id.moviesHomeScreenFragment,
    R.id.liveHomeScreenFragment,
    R.id.tvHomeScreenFragment,
)

const val IS_LIVE = 0
const val IS_MOVIE = 1
const val IS_SERIES = 2

//Item Images
@Parcelize
data class ImagesItem(
    val img: Int,
) : Parcelable

val contentPosters = listOf<ImagesItem>(
    ImagesItem(R.drawable.img_1),
    ImagesItem(R.drawable.img_2),
    ImagesItem(R.drawable.img_3),
    ImagesItem(R.drawable.img_4),
    ImagesItem(R.drawable.img_5),
    ImagesItem(R.drawable.img_1),
    ImagesItem(R.drawable.img_2),
    ImagesItem(R.drawable.img_3),
    ImagesItem(R.drawable.img_1),
    ImagesItem(R.drawable.img_2),
    ImagesItem(R.drawable.img_3),
    ImagesItem(R.drawable.img_4),
    ImagesItem(R.drawable.img_5),
    ImagesItem(R.drawable.img_1),
    ImagesItem(R.drawable.img_2),
    ImagesItem(R.drawable.img_3),
)

//Hero Slider Image
@Parcelize
data class HeroItem(
    val bgImage: Int,
    val title: String,
    val metadata: String,
    val genres: String,
    val description: String,
    val showButtons: Boolean,
) : Parcelable

val heroItems = listOf(
    HeroItem(
        R.drawable.bg_image,
        "Wednesday",
        "2024 • TV-MA",
        "Mystery • Comedy",
        "Smart, sarcastic and a little spooky.",
        showButtons = true
    ), HeroItem(
        R.drawable.slid_1,
        "Coming Soon",
        "2025",
        "Drama",
        "A brand new exclusive title.",
        showButtons = false
    ), HeroItem(
        R.drawable.slid_2,
        "Top Picks",
        "Trending Now",
        "Action",
        "Only the best hand-picked content.",
        showButtons = true
    )
)

//Live Channel Item
@Parcelize
data class AllLiveChannelItem(
    val channelImg: Int,
    val channelName: String,
) : Parcelable

val allLiveChannelItemDummy = listOf<AllLiveChannelItem>(
    AllLiveChannelItem(
        R.drawable.img_profile,
        "colors",
    ), AllLiveChannelItem(
        R.drawable.img_profile,
        "Hiiiii",
    ), AllLiveChannelItem(
        R.drawable.img_profile,
        "Thunder",
    ), AllLiveChannelItem(
        R.drawable.img_profile,
        "NDTV",
    ), AllLiveChannelItem(
        R.drawable.img_profile,
        "DummnyTV",
    ), AllLiveChannelItem(
        R.drawable.img_profile,
        "Cartoon Tv",
    ), AllLiveChannelItem(
        R.drawable.img_profile,
        "Pogo",
    ), AllLiveChannelItem(
        R.drawable.img_profile,
        "Sonic",
    ), AllLiveChannelItem(
        R.drawable.img_profile,
        "Nick",
    )
)

//Search
@Parcelize
data class Category(
    val name: String,
) : Parcelable

val categoryList = listOf<Category>(
    Category("Crime"),
    Category("Adventure"),
    Category("Animated"),
    Category("Comedy"),
    Category("Documentary"),
    Category("Fantasy"),
    Category("Game"),
    Category("Historical"),
    Category("Horror"),
    Category("Musical"),
    Category("Romance"),
    Category("Sad"),
    Category("Love"),
    Category("Movies"),
    Category("Action"),
    Category("News"),
    Category("Kids"),
    Category("Drama"),
    Category("Adventure"),
    Category("Crime"),
    Category("Adventure"),
    Category("Crime"),
    Category("Adventure"),
    Category("Crime")
)

//Movie Sliders
@Parcelize
data class Movie(
    val title: String,
    val genre: String,
    val description: String,
    val poster: Int,
    val background: Int,
) : Parcelable

val dummyMovies = listOf(
    Movie(
        title = "Avatar: The Way of Water",
        genre = "Action • Adventure • Sci-Fi",
        description = "Jake Sully lives with his newfound family formed on the extrasolar moon Pandora. Once a familiar threat returns, Jake must work with Neytiri and the army of the Na’vi race.",
        poster = R.drawable.slid_1,
        background = R.drawable.slid_1
    ), Movie(
        title = "Avengers: Endgame",
        genre = "Action • Superhero",
        description = "After the devastating events of Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more.",
        poster = R.drawable.slid_2,
        background = R.drawable.slid_2
    ), Movie(
        title = "Dune",
        genre = "Sci-Fi • Adventure",
        description = "Paul Atreides leads nomadic tribes in a battle to control the desert planet Arrakis.",
        poster = R.drawable.slid_3,
        background = R.drawable.slid_3
    ), Movie(
        title = "The Batman",
        genre = "Action • Crime • Drama",
        description = "Batman ventures into Gotham City’s underworld when a sadistic killer leaves behind a trail of cryptic clues.",
        poster = R.drawable.slid_1,
        background = R.drawable.slid_1
    ), Movie(
        title = "Interstellar",
        genre = "Adventure • Drama • Sci-Fi",
        description = "A team of explorers travel through a wormhole in space in an attempt to ensure humanity’s survival.",
        poster = R.drawable.slid_2,
        background = R.drawable.slid_2
    )
)

//Movies Category
val dummyMoviesCategory = listOf<Category>(
    Category("Crime"),
    Category("Adventure"),
    Category("Animated"),
    Category("Comedy"),
    Category("Documentary"),
    Category("Fantasy"),
    Category("Game"),
    Category("Historical"),
    Category("Horror"),
    Category("Musical"),
    Category("Romance"),
    Category("Sad"),
    Category("Love"),
    Category("Movies"),
    Category("Action"),
    Category("News"),
    Category("Kids")
)

//Movie Home Screen
@Parcelize
data class Img(
    val img: Int,
    val string: String,
) : Parcelable

@Parcelize
data class StarCastModel(
    val title: String,
    val listOfStarCast: List<Img>,
    var isExpanded: Boolean = false,
) : Parcelable

val starCast = listOf<Img>(
    Img(R.drawable.ct_1, "Robert Downey Jr."),
    Img(R.drawable.ct_2, "Chris Evans"),
    Img(R.drawable.ct_3, "Scarlett Johansson"),
    Img(R.drawable.ct_4, "Robert Downey Jr."),
    Img(R.drawable.ct_5, "Chris Evans"),
    Img(R.drawable.ct_6, "Scarlett Johansson"),
    Img(R.drawable.ct_7, "Robert Downey Jr."),
    Img(R.drawable.ct_8, "Chris Evans"),
    Img(R.drawable.ct_9, "Scarlett Johansson"),
    Img(R.drawable.ct_1, "Robert Downey Jr."),
    Img(R.drawable.ct_2, "Chris Evans"),
    Img(R.drawable.ct_3, "Scarlett Johansson"),
    Img(R.drawable.ct_4, "Robert Downey Jr."),
    Img(R.drawable.ct_5, "Chris Evans"),
    Img(R.drawable.ct_6, "Scarlett Johansson"),
    Img(R.drawable.ct_7, "Robert Downey Jr."),
    Img(R.drawable.ct_8, "Chris Evans"),
    Img(R.drawable.ct_9, "Scarlett Johansson"),
)

const val VIEW_PARENT = 0
const val VIEW_CHILD = 1


fun View.handleDpadNavigation(
    keyCode: Int,
    targetView: View
) {
    setOnKeyListener { _, _, event ->
        if (event.action == KeyEvent.ACTION_DOWN && event.keyCode == keyCode) {
            targetView.requestFocus()
            true
        } else {
            false
        }
    }
}
/*
binding.rvTopMovie.handleDpadNavigation(
    keyCode = KeyEvent.KEYCODE_DPAD_DOWN,
    targetView = binding.rvCategory
)

 */

package com.arny.aircraftrefueling.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.Q
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.arny.aircraftrefueling.R
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Collections
import java.util.Locale
import kotlin.coroutines.CoroutineContext
import kotlin.math.roundToInt

fun <T> Fragment.requestPermission(
    resultLauncher: ActivityResultLauncher<T>,
    permission: String,
    input: T,
    checkPermissionOk: () -> Unit = {}
) {
    when {
        ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED -> {
            checkPermissionOk()
        }

        shouldShowRequestPermissionRationale(permission) -> {
            resultLauncher.launch(input)
        }

        else -> {
            resultLauncher.launch(input)
        }
    }
}

fun Fragment.shareFile(file: File) {
    val uri = FileProvider.getUriForFile(
        requireContext(),
        "${requireContext().packageName}.provider",
        file
    )
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_file_by)))
}

fun <T> Activity.requestPermission(
    resultLauncher: ActivityResultLauncher<T>,
    permission: String,
    input: T,
    checkPermissionOk: () -> Unit = {}
) {
    when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(this, permission) -> {
            checkPermissionOk()
        }

        else -> {
            resultLauncher.launch(input)
        }
    }
}

fun AppCompatActivity.replaceFragment(
    fragment: Fragment, @IdRes frameId: Int,
    addToback: Boolean = false,
    tag: String? = null,
    onLoadFunc: () -> Unit? = {},
    animResourses: Pair<Int, Int>? = null
) {
    val tg = tag ?: fragment.javaClass.simpleName
    supportFragmentManager.transact {
        if (animResourses != null) {
            val slideIn = animResourses.first
            val slideOut = animResourses.second
            setCustomAnimations(slideIn, slideOut)
        }
        replace(frameId, fragment, tg)
        if (addToback) {
            addToBackStack(tag)
        }
    }
    onLoadFunc()
}

fun Fragment.replaceFragment(
    fragment: Fragment, @IdRes frameId: Int,
    addToback: Boolean = false,
    tag: String? = null,
    onLoadFunc: () -> Unit? = {},
    animResourses: Pair<Int, Int>? = null
) {
    val tg = tag ?: fragment.javaClass.simpleName
    childFragmentManager.transact {
        if (animResourses != null) {
            val slideIn = animResourses.first
            val slideOut = animResourses.second
            setCustomAnimations(slideIn, slideOut)
        }
        replace(frameId, fragment, tg)
        if (addToback) {
            addToBackStack(tag)
        }
    }
    onLoadFunc()
}


/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commitAllowingStateLoss()
}

inline fun <reified T : Any> Activity.launchActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivityForResult(intent, requestCode, options)
}

fun Activity.launchIntent(
    requestCode: Int = -1,
    options: Bundle? = null,
    init: Intent.() -> Unit = {}
) {
    val intent = newIntent()
    intent.init()
    startActivityForResult(intent, requestCode, options)
}

fun Activity.launchIntent(
    options: Bundle? = null,
    init: Intent.() -> Unit = {}
) {
    val intent = newIntent()
    intent.init()
    startActivity(intent, options)
}

inline fun <reified T : Any> Fragment.launchActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val context = this.context
    if (context != null) {
        val intent = newIntent<T>(context)
        intent.init()
        startActivityForResult(intent, requestCode, options)
    }
}

inline fun <reified T : Any> Fragment.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val context = this.context
    if (context != null) {
        val intent = newIntent<T>(context)
        intent.init()
        startActivity(intent, options)
    }
}

fun Fragment.launchIntent(
    requestCode: Int = -1,
    options: Bundle? = null,
    init: Intent.() -> Unit = {}
) {
    val context = this.context
    if (context != null) {
        val intent = newIntent()
        intent.init()
        startActivityForResult(intent, requestCode, options)
    }
}

fun Fragment.launchIntent(
    options: Bundle? = null,
    init: Intent.() -> Unit = {}
) {
    val context = this.context
    if (context != null) {
        val intent = newIntent()
        intent.init()
        startActivity(intent, options)
    }
}

inline fun <reified T : Any> Context.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent, options)
}

inline fun <reified T : Any> newIntent(context: Context): Intent = Intent(context, T::class.java)
fun newIntent(): Intent = Intent()

fun Fragment.putExtras(init: Bundle.() -> Unit = {}) {
    val args = Bundle()
    args.init()
    this.arguments = args
}

fun Fragment.putExtras(args: Bundle?) {
    this.arguments = args
}

fun Activity.putExtras(
    resultCode: Int? = null,
    clear: Boolean = true,
    init: Intent.() -> Unit = {}
) {
    val i = if (clear) {
        Intent()
    } else {
        this.intent ?: Intent()
    }
    i.init()
    this.intent = i
    if (resultCode != null) {
        setResult(resultCode, this.intent)
    }
}

fun Activity.shareText(text: String) {
    val shareIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    this.startActivity(Intent.createChooser(shareIntent, "Share with"));
}

fun Activity.sendEmail(email: String, subject: String, body: String, shareTitle: String) {
    val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
    emailIntent.putExtra(Intent.EXTRA_TEXT, body)
//emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body); //If you are using HTML in your body text
    startActivity(Intent.createChooser(emailIntent, shareTitle))
}
/*fun Activity.share(type: String, subject: String, body: String, shareTitle: String) {
    ShareCompat.IntentBuilder.from(this)
            .setType("message/rfc822")
            .addEmailTo(getString(R.string.support_email))
            .setSubject(getString(R.string.app_name))
            .setText("")
            .setChooserTitle(getString(R.string.send_email))
            .startChooser()
}*/

fun Activity.shareLocation(lat: Double, long: Double, label: String) {
    this.startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("geo:<$lat>,<$long>?q=<$lat>,<$long>($label)")
        )
    );
}

fun Activity.shareImage(uri: Uri) {
    val shareIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, uri)
        type = "image/jpeg"
    }
    this.startActivity(Intent.createChooser(shareIntent, "Share with"));
}

fun Activity.shareFileWithType(uri: Uri, fileType: String) {
    StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().build())
    val shareIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, uri)
        type = fileType
        if (Build.VERSION.SDK_INT >= Q) {
            addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        }
    }
    this.startActivity(Intent.createChooser(shareIntent, "Share with"));
}

enum class AnimType {
    DEFAULT, TOP_BOTTOM, LEFT_RIGHT
}

fun animateVisible(
    v: View,
    visible: Boolean,
    duration: Int,
    onComplete: () -> Unit? = {},
    interpolator: TimeInterpolator? = null,
    type: AnimType = AnimType.DEFAULT
) {
    v.clearAnimation()
    val animate = v.animate()
    if (interpolator != null) {
        animate.interpolator = interpolator
    }
    animate.duration = duration.toLong()
    when (type) {
        AnimType.DEFAULT -> {
            animate.alpha(if (visible) 1.0f else 0.0f)
            animate.setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    v.setVisible(visible)
                    onComplete.invoke()
                }
            })
        }

        AnimType.TOP_BOTTOM -> {
            v.pivotY = 0f
            if (visible) {
                v.scaleY = 0.0f
                v.setVisible(true)
                animate.scaleY(1.0f)
                animate.setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        onComplete.invoke()
                    }
                })
            } else {
                v.scaleY = 1.0f
                animate.scaleY(0.0f)
                animate.setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        v.setVisible(visible)
                        onComplete.invoke()
                    }
                })
            }
        }

        AnimType.LEFT_RIGHT -> {
            v.pivotX = 0f
            v.scaleX = if (visible) 0.0f else 1.0f
            v.setVisible(visible)
            animate.scaleX(if (visible) 1.0f else 0.0f)
            animate.setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    onComplete.invoke()
                }
            })
        }
    }
}

fun Intent?.hasExtra(extraName: String): Boolean {
    return this?.hasExtra(extraName) ?: false
}

inline fun <reified T> Intent?.getExtra(extraName: String): T? {
    if (this.hasExtra(extraName)) {
        return this?.extras?.get(extraName) as? T
    }
    return null
}

inline fun <reified T> Activity?.getExtra(extraName: String): T? {
    val intent = this?.intent
    if (intent.hasExtra(extraName)) {
        return intent?.extras?.get(extraName) as? T
    }
    return null
}

inline fun <reified T> Fragment?.getExtra(extraName: String): T? {
    return this?.arguments?.get(extraName) as? T
}

inline fun <reified T> Bundle?.getExtra(extraName: String): T? {
    return this?.get(extraName) as? T
}

fun runOnUI(func: () -> Unit? = {}) = Handler(Looper.getMainLooper()).post { func.invoke() }

@JvmOverloads
fun runOnLooper(func: () -> Unit? = {}, looper: Looper = Looper.getMainLooper()) =
    Handler(looper).post { func.invoke() }

fun View?.setVisible(visible: Boolean) {
    this?.visibility = if (visible) View.VISIBLE else View.GONE
}

fun View?.setVisible(
    visible: Boolean,
    duration: Int,
    onComplete: () -> Unit?,
    interpolator: TimeInterpolator? = null,
    type: AnimType = AnimType.DEFAULT
) {
    this?.let { animateVisible(it, visible, duration, onComplete, interpolator, type) }
}

fun TextView?.setString(text: String?): TextView? {
    this?.clearFocus()
    this?.tag = ""
    this?.text = text
    this?.tag = null
    return this
}

fun animateVisible(v: View, visible: Boolean, duration: Int) {
    val alpha = if (visible) 1.0f else 0.0f
    v.clearAnimation()
    v.animate()
        .alpha(alpha)
        .setDuration(duration.toLong())
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                v.setVisible(visible)
            }
        })
}

fun AppCompatActivity.backStackCnt(): Int {
    return supportFragmentManager.backStackEntryCount
}

fun AppCompatActivity.backStackClear() {
    return supportFragmentManager.popBackStack(
        null,
        androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
    );
}

fun AppCompatActivity.resetFragmentsInManager() {
    val fragments = supportFragmentManager.fragments
    for (curFrag in fragments) {
        if (curFrag != null) {
            supportFragmentManager.beginTransaction().remove(curFrag).commitAllowingStateLoss()
        }
    }
}

fun AppCompatActivity.replaceFragmentInActivity(
    fragment: Fragment,
    @IdRes frameId: Int,
    tag: String? = null
) {
    supportFragmentManager.transact {
        replace(frameId, fragment, tag)
    }
}

fun AppCompatActivity.addFragmentToActivity(fragment: Fragment, @IdRes frameId: Int, tag: String?) {
    supportFragmentManager.transact {
        add(frameId, fragment, tag)
    }
}

fun AppCompatActivity.popBackStack(immadiate: Boolean = true) {
    if (immadiate) {
        supportFragmentManager.popBackStackImmediate()
    } else {
        supportFragmentManager.popBackStack()
    }
}

fun checkPremission(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun AppCompatActivity.getFragment(position: Int): Fragment? {
    return supportFragmentManager.fragments.getOrNull(position)
}

fun AppCompatActivity.fragmentBackStackCnt(): Int {
    return supportFragmentManager.backStackEntryCount
}

fun AppCompatActivity.fragmentsBackStack(): Fragment {
    val fragments = supportFragmentManager.fragments
    val size = fragments.size
    val fragment = fragments.get(size - 1)
    return fragment
}

fun AppCompatActivity.fragmentBackStackClear() {
    return supportFragmentManager.popBackStack(
        null,
        androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
    );
}

fun inflate(inflater: LayoutInflater, container: ViewGroup?, @LayoutRes resource: Int): View? {
    return inflater.inflate(resource, container, false)
}

fun AppCompatActivity.getFragmentInContainer(@IdRes containerId: Int): Fragment? {
    return supportFragmentManager.findFragmentById(containerId)
}

fun AppCompatActivity.getFragmentByTag(tag: String?): Fragment? {
    return supportFragmentManager.findFragmentByTag(tag)
}

fun AppCompatActivity.setupActionBar(
    @IdRes toolbarId: Int,
    action: (ActionBar?.() -> Unit)? = null
) {
    setSupportActionBar(findViewById(toolbarId))
    supportActionBar?.run {
        action?.let { it() }
    }
}

fun AppCompatActivity.setupActionBar(toolbar: Toolbar?, action: (ActionBar?.() -> Unit)? = null) {
    setSupportActionBar(toolbar)
    supportActionBar?.run {
        action?.let { it() }
    }
}

fun View.showSnackBar(message: String?, duration: Int = Snackbar.LENGTH_SHORT) {
    message?.let {
        Snackbar.make(this, message, duration).show()
    }
}

@SuppressLint("ClickableViewAccessibility")
fun EditText.setDrawableRightClick(onClick: () -> Unit) {
    this.setOnTouchListener { v, event ->
        if (v is TextView) {
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (v.right - v.compoundDrawables[2].bounds.width())) {
                    onClick.invoke()
                    return@setOnTouchListener true
                }
            }
        }
        return@setOnTouchListener false
    }
}

fun View.showSnackBar(
    message: String,
    actionText: String,
    duration: Int? = null,
    @ColorInt actionColor: Int? = null,
    action: () -> Unit
) {
    val snackBar = Snackbar.make(this, message, duration ?: Snackbar.LENGTH_INDEFINITE)
    snackBar.setAction(actionText) { action.invoke() }
    if (actionColor != null) {
        snackBar.setActionTextColor(actionColor)
    }
    snackBar.show()
}

inline fun <reified T> getIntentExtra(intent: Intent?, extraName: String): T? {
    return intent?.extras?.get(extraName) as? T
}

inline fun <reified T> getBundleExtra(extras: Bundle?, extraName: String): T? {
    return extras?.get(extraName) as? T
}

fun isKeyboardVisible(context: Context): Boolean {
    val imm by lazy { context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }
    val windowHeightMethod =
        InputMethodManager::class.java.getMethod("getInputMethodWindowVisibleHeight")
    val height = windowHeightMethod.invoke(imm) as Int
    return height > 100
}

fun Context.getSizeDP(size: Int): Int {
    return (size * this.resources.displayMetrics.density).roundToInt()
}

@ColorInt
fun Context.getIntColor(@ColorRes res: Int): Int {
    return ContextCompat.getColor(this, res)
}

fun Context.getResDrawable(@DrawableRes res: Int): Drawable? {
    return ContextCompat.getDrawable(this, res)
}

fun ImageView?.setSrcTintColor(@DrawableRes src: Int, @ColorInt color: Int) {
    val drawable = this?.context?.let { ContextCompat.getDrawable(it, src) }
    if (drawable != null) {
        val wrapped = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(wrapped, color)
        this?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }
}

fun getHexColor(color: Int): String {
    return String.format("#%06X", (0xFFFFFF and color))
}

fun String.sFormat(vararg args: Any?): String {
    return String.format(Locale.getDefault(), this, args)
}

@RequiresApi(Build.VERSION_CODES.O)
private fun createNotificationChannel(context: Context): String {
    val channelId = "my_service"
    val channelName = "My Background Service"
    val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
    chan.lightColor = Color.BLUE
    chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
    val service = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
    service?.createNotificationChannel(chan)
    return channelId
}

private fun getServiceNotification(
    context: Context,
    cls: Class<*>,
    requestCode: Int,
    title: String,
    content: String,
    icon: Int
)
        : Notification {
    val notification: Notification
    val notificationIntent = Intent(context, cls)
    notificationIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
    val pendingIntent = PendingIntent.getActivity(
        context, requestCode, notificationIntent,
        PendingIntent.FLAG_MUTABLE
    )
    val notifbuild = getNotifBuilder(context)
    notifbuild.setSmallIcon(icon)// маленькая иконка
        .setAutoCancel(false)
        .setContentTitle(title)// Заголовок уведомления
        .setContentText(content) // Текст уведомления
    notifbuild.setContentIntent(pendingIntent)
    notification = notifbuild.build()
    return notification
}

private fun getNotifBuilder(context: Context): Notification.Builder {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Notification.Builder(context, createNotificationChannel(context))
    } else {
        Notification.Builder(context)
    }
}

@SuppressLint("RestrictedApi")
fun BottomNavigationView?.disableShiftMode() {
    val menuView = this?.getChildAt(0) as? BottomNavigationMenuView
    if (menuView != null) {
        try {
            val fld = "isShifting"//"mShiftingMode"
            val shiftingMode = menuView.javaClass.getDeclaredField(fld)
            shiftingMode.isAccessible = true
            shiftingMode.setBoolean(menuView, false)
            shiftingMode.isAccessible = false
            for (i in 0 until menuView.childCount) {
                val item = menuView.getChildAt(i) as BottomNavigationItemView

                item.setShifting(false)
                // set once again checked value, so view will be updated
                item.setChecked(item.itemData?.isChecked == true)
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }
}

fun <T> find(list: List<T>, c: T, comp: Comparator<T>): T? {
    return list.firstOrNull { comp.compare(c, it) == 0 }
}

fun <T> findPosition(list: List<T>, item: T): Int {
    return list.indexOf(item)
}

fun <T> findPosition(list: Array<T>, item: T): Int {
    return list.indexOf(item)
}

fun <T> getExcludeList(
    list: ArrayList<T>,
    items: List<T>,
    comparator: Comparator<T>
): ArrayList<T> {
    val res = ArrayList<T>()
    for (t in list) {
        val pos = Collections.binarySearch(items, t, comparator)
        if (pos < 0) {
            res.add(t)
        }
    }
    return res
}

fun getSQLType(fieldType: String): String {
    val res = when {
        fieldType.equals("int", true) -> "INTEGER"
        fieldType.equals("integer", true) -> "INTEGER"
        fieldType.equals("float", true) -> "REAL"
        fieldType.equals("double", true) -> "REAL"
        fieldType.equals("string", true) -> "TEXT"
        fieldType.equals("char", true) -> "TEXT"
        fieldType.equals("byte", true) -> "TEXT"
        else -> "TEXT"
    }
    return res
}

/**
 * Универсальная функция окончаний
 * @param [count] число
 * @param [zero_other] слово с окончанием значения  [count] либо ноль,либо все остальные варианты включая от 11 до 19 (слов)
 * @param [one] слово с окончанием значения  [count]=1 (слово)
 * @param [two_four] слово с окончанием значения  [count]=2,3,4 (слова)
 */
fun getTermination(count: Int, zero_other: String, one: String, two_four: String): String {
    if (count % 100 in 11..19) {
        return "$count $zero_other"
    }
    return when (count % 10) {
        1 -> "$count $one"
        2, 3, 4 -> "$count $two_four"
        else -> "$count $zero_other"
    }
}

/**
 * safety String? to Double
 */
fun String?.setDouble(): Double {
    val source = this ?: ""
    if (source.isBlank() || source == ".") {
        return 0.0
    }
    return source.toDouble()
}

fun String?.ifEmpty(default: String): String {
    val blank = this?.isBlank() ?: true
    return if (blank) default else this!!
}

/**
 * return items from second array wich not includes in first by custom diff in predicate
 * @param first ArrayList of  T
 * @param second Collection of  T
 * @param predicate function equals
 */
fun <T> arraysDiff(
    first: ArrayList<T>?,
    second: ArrayList<T>,
    fillAll: Boolean = false,
    predicate: (firstItem: T, secondItem: T) -> Boolean
): ArrayList<T> {
    if (first.isNullOrEmpty()) return second
    if (first.isEmpty() && second.isNotEmpty()) return second
    val result = arrayListOf<T>()
    val secondTemp = arrayListOf<T>()
    secondTemp.addAll(second)
    for (f in first) {
        var equal = false
        for (s in second) {
            if (predicate.invoke(f, s)) {
                secondTemp.remove(s)
                equal = true
                break
            }
        }
        if (!equal) {
            result.add(f)
        }
    }
    if (fillAll) {
        result.addAll(secondTemp)
    }
    return result
}

fun <T> Collection<T>.filterList(predicate: (T) -> Boolean): ArrayList<T> {
    return ArrayList(this.filter(predicate))
}

fun <T : Any> Collection<T>?.dump(predicate: (cls: T) -> String?): String {
    return dumpArray(this, predicate)
}

fun <T : Any> dumpArray(collection: Collection<T>?, predicate: (cls: T) -> String?): String {
    var res = ""
    if (collection == null) {
        res += "Collection is null"
        return res
    }
    if (collection.isEmpty()) {
        res += "Collection is empty"
        return res
    }
    for (ind in collection.withIndex()) {
        val index = ind.index
        val value = ind.value
        if (index == 0) {
            res += "${value.javaClass.name}\n"
            res += predicate.invoke(value)
        } else {
            res += "\n"
            res += predicate.invoke(value)
        }
    }
    return res
}

/**
 * Универсальная функция окончаний
 * @param [count] число
 * @param [zero_other] слово с окончанием значения  [count] либо ноль,либо все остальные варианты включая от 11 до 19 (слов)
 * @param [one] слово с окончанием значения  [count]=1 (слово)
 * @param [two_four] слово с окончанием значения  [count]=2,3,4 (слова)
 */
fun getTermination(
    count: Int,
    zero_other: String,
    one: String,
    two_four: String,
    concat: Boolean = true
): String {
    if (count % 100 in 11..19) {
        return if (concat) "$count $zero_other" else " $zero_other"
    }
    return when (count % 10) {
        1 -> if (concat) "$count $one" else one
        2, 3, 4 -> if (concat) "$count $two_four" else two_four
        else -> if (concat) "$count $zero_other" else zero_other
    }
}

fun String?.isEmpty(): Boolean = this.isNullOrBlank()

/**
 * Extended function to check empty
 */
fun Any?.empty(): Boolean {
    return when {
        this == null -> true
        this is String && this == "null" -> true
        this is String -> this.isBlank()
        this is Iterable<*> -> this.asIterable().none()
        this is Collection<*> -> this.isEmpty()
        else -> false
    }
}

fun <T> Collection<T>.copy(): ArrayList<T> {
    val newList = ArrayList<T>()
    newList.addAll(this)
    return newList
}

fun <T> launchAsync(
    block: suspend () -> T,
    onComplete: (T) -> Unit = {},
    onError: (Throwable) -> Unit = {},
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    context: CoroutineContext = Dispatchers.Main + SupervisorJob(),
    onCanceled: () -> Unit = {}
): Job {
    val scope = CoroutineScope(context)
    return scope.launch {
        try {
            val result = withContext(dispatcher) { block.invoke() }
            onComplete.invoke(result)
        } catch (e: CancellationException) {
            onCanceled()
        } catch (e: Exception) {
            onError(e)
        }
    }
}

fun String?.parseLong(): Long? {
    return when {
        this == null -> null
        this.isBlank() -> null
        else -> {
            try {
                this.toLong()
            } catch (e: Exception) {
                null
            }
        }
    }
}

fun String?.parseDouble(): Double? {
    return when {
        this == null -> null
        this.isBlank() -> null
        else -> {
            try {
                this.toDouble()
            } catch (e: Exception) {
                null
            }
        }
    }
}

fun String?.parseInt(): Int? {
    return when {
        this == null -> null
        this.isBlank() -> null
        else -> {
            try {
                this.toInt()
            } catch (e: Exception) {
                null
            }
        }
    }
}

fun String?.parseInt(default: Int = 0): Int {
    return when {
        this == null -> default
        this.isBlank() -> default
        else -> {
            try {
                this.toInt()
            } catch (e: Exception) {
                default
            }
        }
    }
}
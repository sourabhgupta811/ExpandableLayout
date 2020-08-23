# ExpandableLayout
Easy to use ExpandableLayout for Android.

<br><b>Sample included-</b>
<ul>
<li>
    <a href="https://github.com/sourabhgupta811/ExpandableLayout/blob/master/app/src/main/java/com/samnetworks/expandablelayout/RecyclerViewAdapter.kt">ExpandableLayout in RecyclerView</a>
</li></ul><br><br>
<b>How to use</b><br>
<i>In xml-</i>

```
<com.samnetworks.expandable_layout.base.ExpandableLayout android:layout_width="match_parent"
    android:id="@+id/expandable_layout"
    android:layout_height="wrap_content"
    xmlns:android="http://schemas.android.com/apk/res/android" />
```

<i>Initialize in java/kotlin</i>

```
    val bindings = expandableLayout.initView<HeaderViewBinding,ExpandableViewBinding>(R.layout.header_view,R.layout.expandable_view)
    headerBinding = bindings.first
    expandableBinding = bindings.second
```

<i>Expand/Collapse-</i>

```
    expandableLayout.expand(@Optional duration)
    expandableLayout.collapse(@Optional duration)
```

That's it.

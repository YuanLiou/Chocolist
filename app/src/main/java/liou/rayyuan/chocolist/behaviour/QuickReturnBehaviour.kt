package liou.rayyuan.chocolist.behaviour

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View

class QuickReturnBehaviour(context: Context, attr: AttributeSet) : CoordinatorLayout.Behavior<View>(context, attr) {

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        val oldTranslation = child.translationY
        val newTranslation = oldTranslation + dy

        when {
            newTranslation > child.height -> child.translationY = child.height.toFloat()
            newTranslation < 0 -> child.translationY = 0f
            else -> child.translationY = newTranslation
        }
    }
}

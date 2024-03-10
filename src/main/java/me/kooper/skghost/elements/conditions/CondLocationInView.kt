package me.kooper.skghost.elements.conditions

import ch.njol.skript.Skript
import ch.njol.skript.lang.Condition
import ch.njol.skript.lang.Expression
import ch.njol.skript.lang.SkriptParser
import ch.njol.util.Kleenean
import io.papermc.paper.math.Position
import me.kooper.ghostcore.data.ChunkedViewData
import me.kooper.ghostcore.data.SimplePosition
import me.kooper.ghostcore.data.ViewData
import org.bukkit.Location
import org.bukkit.event.Event


@Suppress("UnstableApiUsage")
class CondLocationInView : Condition() {

    companion object {
        init {
            Skript.registerCondition(
                CondLocationInView::class.java,
                "%location% (1¦is|2¦is(n't| not)) inside [of] view %view%"
            )
        }
    }

    private lateinit var location: Expression<Location>
    private lateinit var view: Expression<ChunkedViewData>

    @Suppress("UNCHECKED_CAST")
    override fun init(
        expressions: Array<Expression<*>>,
        matchedPattern: Int,
        isDelayed: Kleenean?,
        parser: SkriptParser.ParseResult?
    ): Boolean {
        location = expressions[0] as Expression<Location>
        view = expressions[1] as Expression<ChunkedViewData>
        isNegated = parser!!.mark == 1
        return true
    }

    override fun toString(event: Event?, debug: Boolean): String {
        return "Block inside view: ${location.toString(event, debug)} ${
            view.toString(
                event,
                debug
            )
        }"
    }

    override fun check(event: Event?): Boolean {
        if (location.getSingle(event) == null || view.getSingle(event) == null) return !isNegated
        val loc = location.getSingle(event)!!
        return if (view.getSingle(event)!!.hasBlock(SimplePosition.from(loc.blockX, loc.blockY, loc.blockZ))) isNegated else !isNegated
    }

}
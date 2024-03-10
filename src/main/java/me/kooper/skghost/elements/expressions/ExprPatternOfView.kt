package me.kooper.skghost.elements.expressions

import ch.njol.skript.Skript
import ch.njol.skript.lang.Expression
import ch.njol.skript.lang.ExpressionType
import ch.njol.skript.lang.SkriptParser
import ch.njol.skript.lang.util.SimpleExpression
import ch.njol.util.Kleenean
import me.kooper.ghostcore.data.ChunkedViewData
import me.kooper.ghostcore.data.ViewData
import me.kooper.ghostcore.models.Stage
import me.kooper.skghost.utils.Utils
import org.bukkit.event.Event

class ExprPatternOfView : SimpleExpression<String>() {

    private lateinit var view: Expression<ChunkedViewData>
    private lateinit var stage: Expression<Stage>

    companion object {
        init {
            Skript.registerExpression(
                ExprPatternOfView::class.java,
                String::class.java, ExpressionType.COMBINED, "[the] pattern of view %view% (of|in) stage %stage%"
            )
        }
    }

    override fun toString(event: Event?, debug: Boolean): String {
        return "Pattern of view expression with expression view: ${view.toString(event, debug)}, expression stage: ${stage.toString(event, debug)}"
    }

    @Suppress("UNCHECKED_CAST")
    override fun init(expressions: Array<out Expression<*>>?, matchedPattern: Int, isDelayed: Kleenean?, parser: SkriptParser.ParseResult?): Boolean {
        view = expressions!![0] as Expression<ChunkedViewData>
        stage = expressions[1] as Expression<Stage>
        return true
    }

    override fun isSingle(): Boolean {
        return true
    }

    override fun getReturnType(): Class<out String> {
        return String::class.java
    }

    override fun get(event: Event?): Array<String?> {
        if (view.getSingle(event) == null || stage.getSingle(event) == null) return arrayOf(null)
        val stage: Stage = stage.getSingle(event)!!
        val view: ChunkedViewData = view.getSingle(event)!!
        return arrayOf(Utils.createMaterialValuesString(stage.views[view.name]!!.patternData.blockDataPercentages))
    }

}
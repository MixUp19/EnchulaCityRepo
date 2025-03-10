package com.example.emchulacity.util

import androidx.compose.ui.graphics.Color
import com.example.emchulacity.R
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.CubeNode
import io.github.sceneview.node.ModelNode

object Utils {
    var currentModel = "models/calle.glb"

    val models = listOf(
        Model3d("Calle", "models/calle.glb", R.drawable.calle),
        Model3d("Calle 2", "models/calle2.glb", R.drawable.calle2),
        Model3d("cebra", "models/cebra.glb", R.drawable.cebra),
        Model3d("perro", "models/dog.glb", R.drawable.dog),
        Model3d("Calle", "models/calle.glb", R.drawable.calle),
        Model3d("Calle 2", "models/calle2.glb", R.drawable.calle2),
        Model3d("cebra", "models/cebra.glb", R.drawable.cebra),
        Model3d("perro", "models/dog.glb", R.drawable.dog),
    )

    fun setModel(modelSelected: String){
        currentModel = modelSelected
    }

    fun getModel(): String {
        return currentModel
    }

    fun createAnchorNode(
        engine: Engine,
        modelLoader: ModelLoader,
        materialLoader: MaterialLoader,
        modelInstance: MutableList<ModelInstance>,
        anchor: Anchor,
        model: String
    ): AnchorNode {
        val anchorNode = AnchorNode(engine = engine, anchor = anchor)
        val modelNode = ModelNode(
            modelInstance = modelInstance.apply {
                if (isEmpty()) {
                    this += modelLoader.createInstancedModel(model, 10)
                }
            }.removeAt(modelInstance.size -1),
            scaleToUnits = 1f
        ).apply {
            isEditable = true
        }
        val boundingBox = CubeNode(
            engine = engine,
            size = modelNode.extents,
            center = modelNode.center,
            materialInstance = materialLoader.createColorInstance(Color.White)
        ).apply {
            isVisible = false
        }
        modelNode.addChildNode(boundingBox)
        anchorNode.addChildNode(modelNode)
        listOf(modelNode, anchorNode).forEach {
            it.onEditingChanged = { editingTransforms ->
                boundingBox.isVisible = editingTransforms.isNotEmpty()
            }
        }
        return anchorNode

    }

}
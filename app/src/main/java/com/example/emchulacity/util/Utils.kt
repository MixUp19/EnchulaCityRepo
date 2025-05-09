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
    var scale = 1f

    val models = listOf(
        Model3d("Calle", "models/calle.glb", R.drawable.calle),
        Model3d("Calle 2", "models/calle2.glb", R.drawable.calle2),
        Model3d("cebra", "models/cebra.glb", R.drawable.cebra),
        Model3d("perro", "models/dog.glb", R.drawable.dog),
        Model3d("Banca Incluyente", "models/Banca2.glb", R.drawable.bancaincluyente),
        Model3d("Banca", "models/bancabonita.glb", R.drawable.banca),
        Model3d("Estación Bicicleta", "models/EstacionBicis.glb", R.drawable.bicicletas, 13f),
        Model3d("Calle Autobus", "models/calle con espacio lateral de autobus.glb", R.drawable.calle),
        Model3d("Rampa", "models/RampaElaborada.glb", R.drawable.rampa),
        Model3d("Rampa 2", "models/rampadiscapacitado.glb", R.drawable.rampa),
        Model3d("Tope ciclovia", "models/TopeCiclovia.glb", R.drawable.tope),
        Model3d("Superficie Banqueta", "models/superficie_banqueta.glb", R.drawable.piso_banqueta),
        Model3d("Lampara","models/lampara con luz.glb", R.drawable.lampara),
        Model3d("Arbol", "models/Arbol.glb", R.drawable.arbol, 5f),
        Model3d("Stop", "models/STOP.glb", R.drawable.alto),
        Model3d("No fumar", "models/smoken't.glb", R.drawable.fumar)
    )

    fun setModel(modelSelected: String, scaleSelected: Float){
        currentModel = modelSelected
        scale = scaleSelected
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
            scaleToUnits = scale
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
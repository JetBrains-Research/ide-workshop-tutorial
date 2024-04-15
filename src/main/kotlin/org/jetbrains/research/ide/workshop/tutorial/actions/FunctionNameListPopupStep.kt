package org.jetbrains.research.ide.workshop.tutorial.actions

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.popup.PopupStep
import com.intellij.openapi.ui.popup.util.BaseListPopupStep
import com.intellij.psi.PsiFile
import com.intellij.refactoring.rename.RenameProcessor
import org.jetbrains.kotlin.psi.KtNamedFunction

class FunctionNameListPopupStep(
    title: String,
    methodNameSuggestions: List<String>,
    private var editor: Editor,
    private val psiFile: PsiFile,
) : BaseListPopupStep<String>(title, methodNameSuggestions) {

    override fun onChosen(selectedValue: String, finalChoice: Boolean): PopupStep<*>? {
        doRenameMethodRefactoring(selectedValue)
        return super.onChosen(selectedValue, finalChoice)
    }

    private fun doRenameMethodRefactoring(selectedValue: String) {
        val elementAt = psiFile.findElementAt(editor.caretModel.offset) ?: return
        val functionToRename = elementAt.parent
        if (functionToRename is KtNamedFunction && functionToRename.name != selectedValue) {
            RenameProcessor(elementAt.project, functionToRename, selectedValue, false, false).run()
        }
    }
}
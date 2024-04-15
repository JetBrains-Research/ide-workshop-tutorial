package org.jetbrains.research.ide.workshop.tutorial.actions

import org.jetbrains.research.ide.workshop.tutorial.llm.RequestProvider
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.psi.PsiElement
import kotlinx.coroutines.runBlocking
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.research.ide.workshop.tutorial.MyBundle

class GenerateFunctionNameAction : PsiElementBaseIntentionAction() {
    override fun getText() = MyBundle.message("generate.function.name.action.text")

    override fun getFamilyName() = text

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        return element.parent is KtNamedFunction
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        element.let {
            val suggestionsList: List<String> =
                runBlocking { RequestProvider().sendRequest(element.text) }
            val listPopup =
                JBPopupFactory.getInstance().createListPopup(
                    FunctionNameListPopupStep(
                        MyBundle.message("generate.function.name.popup.text"),
                        suggestionsList,
                        editor!!,
                        element.containingFile
                    )
                )
            listPopup.showInBestPositionFor(editor)
        }
    }

    override fun startInWriteAction() = true
}
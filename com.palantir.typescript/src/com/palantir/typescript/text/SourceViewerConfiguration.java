/*
 * Copyright 2013 Palantir Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.palantir.typescript.text;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

/**
 * Configures the features of the editor. This is the entry point for features like intelligent
 * double click, auto completion, and syntax highlighting.
 *
 * @author tyleradams
 */
public final class SourceViewerConfiguration extends TextSourceViewerConfiguration {

    private final ColorManager colorManager;

    public SourceViewerConfiguration(ColorManager colorManager) {
        checkNotNull(colorManager);

        this.colorManager = colorManager;
    }

    @Override
    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
        checkNotNull(sourceViewer);

        ContentAssistant assistant = new ContentAssistant();

        assistant.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
        assistant.setContentAssistProcessor(new ContentAssistProcessor(), IDocument.DEFAULT_CONTENT_TYPE);

        assistant.enableAutoActivation(true);
        assistant.setAutoActivationDelay(100);
        assistant.setProposalPopupOrientation(IContentAssistant.PROPOSAL_OVERLAY);
        Shell parent = null;
        IInformationControlCreator creator = new DefaultInformationControl(parent).getInformationPresenterControlCreator();
        assistant.setInformationControlCreator(creator); //TODO: Why does this work?
        return assistant;
    }

    @Override
    public IContentFormatter getContentFormatter(ISourceViewer sourceViewer) {
        return new ContentFormatter();
    }

    @Override
    public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
        return new PresentationReconciler(this.colorManager);
    }
}

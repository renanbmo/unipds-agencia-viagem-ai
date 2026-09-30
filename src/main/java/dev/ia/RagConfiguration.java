package dev.ia;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.function.Supplier;

@ApplicationScoped
public class RagConfiguration implements Supplier<RetrievalAugmentor> {

    @Inject
    EmbeddingStore<TextSegment> embeddingStore;

    @Inject
    EmbeddingModel embeddingModel;

    @Override
    public RetrievalAugmentor get() {
        return DefaultRetrievalAugmentor.builder()
                .contentRetriever(EmbeddingStoreContentRetriever.builder()
                        .embeddingStore(embeddingStore)
                        .embeddingModel(embeddingModel)
                        .maxResults(5)
                        .build())
                .contentInjector(DefaultContentInjector.builder()
                        .promptTemplate(PromptTemplate.from("""
                            {{userMessage}}

                            Use as informações abaixo, extraídas dos documentos oficiais da empresa, para responder:
                            {{contents}}
                            """))
                        .build())
                .build();
    }
}

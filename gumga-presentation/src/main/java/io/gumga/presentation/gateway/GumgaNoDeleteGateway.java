package io.gumga.presentation.gateway;

import io.gumga.application.service.GumgaNoDeleteService;
import io.gumga.core.GumgaIdable;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.core.utils.ReflectionUtils;
import io.gumga.domain.service.GumgaReadableServiceable;
import io.gumga.domain.service.GumgaWritableServiceable;
import io.gumga.presentation.GumgaTranslator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class GumgaNoDeleteGateway<A extends GumgaIdable<?>, DTO> implements GumgaReadableServiceable<DTO>, GumgaWritableServiceable<DTO> {

	@Autowired
	private GumgaNoDeleteService<A, ?> delegate;
	
	@Autowired
	private GumgaTranslator<A, DTO> translator;
	
	@Override
	public SearchResult<DTO> pesquisa(QueryObject query) {
		SearchResult<A> pesquisa = delegate.pesquisa(query);
		return new SearchResult<>(query, pesquisa.getCount(), translator.from((List<A>) pesquisa.getValues()));
	}

	@Override
	public DTO view(Long id) {
		return translator.from(delegate.view(id));
	}

	@Override
	public DTO save(DTO resource) {
		return translator.from(delegate.save(translator.to(resource)));
	}
	
	@SuppressWarnings("unchecked")
	public Class<DTO> clazz() {
		return (Class<DTO>) ReflectionUtils.inferGenericType(getClass());
	}

}

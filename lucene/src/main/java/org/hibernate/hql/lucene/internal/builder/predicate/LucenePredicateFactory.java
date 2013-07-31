/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * JBoss, Home of Professional Open Source
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.hibernate.hql.lucene.internal.builder.predicate;

import java.util.List;

import org.apache.lucene.search.Query;
import org.hibernate.hql.ast.spi.predicate.ComparisonPredicate;
import org.hibernate.hql.ast.spi.predicate.ComparisonPredicate.Type;
import org.hibernate.hql.ast.spi.predicate.ConjunctionPredicate;
import org.hibernate.hql.ast.spi.predicate.DisjunctionPredicate;
import org.hibernate.hql.ast.spi.predicate.NegationPredicate;
import org.hibernate.hql.ast.spi.predicate.PredicateFactory;
import org.hibernate.hql.ast.spi.predicate.RangePredicate;
import org.hibernate.hql.ast.spi.predicate.RootPredicate;
import org.hibernate.hql.internal.util.Strings;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.QueryContextBuilder;

/**
 * Factory creating predicate instances based on Lucene.
 *
 * @author Gunnar Morling
 */
public class LucenePredicateFactory implements PredicateFactory<Query> {

	private final QueryContextBuilder queryContextBuilder;
	private QueryBuilder queryBuilder;

	public LucenePredicateFactory(QueryContextBuilder queryContextBuilder) {
		this.queryContextBuilder = queryContextBuilder;
	}

	@Override
	public RootPredicate<Query> getRootPredicate(Class<?> entityType) {
		queryBuilder = queryContextBuilder.forEntity( entityType ).get();
		return new LuceneRootPredicate( queryBuilder );
	}

	@Override
	public ComparisonPredicate<Query> getComparisonPredicate(Class<?> entityType, Type comparisonType, List<String> propertyPath, Object value) {
		return new LuceneComparisonPredicate( queryBuilder, Strings.join( propertyPath, "." ), comparisonType, value );
	}

	@Override
	public RangePredicate<Query> getRangePredicate(Class<?> entityType, List<String> propertyPath, Object lowerValue, Object upperValue) {
		return new LuceneRangePredicate( queryBuilder, Strings.join( propertyPath, "." ), lowerValue, upperValue );
	}

	@Override
	public NegationPredicate<Query> getNegationPredicate() {
		return new LuceneNegationPredicate( queryBuilder );
	}

	@Override
	public DisjunctionPredicate<Query> getDisjunctionPredicate() {
		return new LuceneDisjunctionPredicate( queryBuilder );
	}

	@Override
	public ConjunctionPredicate<Query> getConjunctionPredicate() {
		return new LuceneConjunctionPredicate( queryBuilder );
	}
}

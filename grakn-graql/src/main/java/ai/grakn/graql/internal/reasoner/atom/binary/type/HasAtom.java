/*
 * Grakn - A Distributed Semantic Database
 * Copyright (C) 2016  Grakn Labs Limited
 *
 * Grakn is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Grakn is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Grakn. If not, see <http://www.gnu.org/licenses/gpl.txt>.
 */

package ai.grakn.graql.internal.reasoner.atom.binary.type;

import ai.grakn.graql.Var;
import ai.grakn.graql.VarPattern;
import ai.grakn.graql.admin.Atomic;
import ai.grakn.graql.admin.ReasonerQuery;
import ai.grakn.graql.admin.Unifier;
import ai.grakn.graql.admin.VarProperty;
import ai.grakn.graql.internal.reasoner.atom.Atom;
import ai.grakn.graql.internal.pattern.property.HasAttributeTypeProperty;
import ai.grakn.graql.internal.reasoner.atom.binary.TypeAtom;
import ai.grakn.graql.internal.reasoner.atom.predicate.IdPredicate;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * <p>
 * TypeAtom corresponding to graql a {@link HasAttributeTypeProperty} property.
 * </p>
 *
 * @author Kasper Piskorski
 *
 */
public class HasAtom extends TypeAtom {

    public HasAtom(VarPattern pattern, Var predicateVar, IdPredicate p, ReasonerQuery par) { super(pattern, predicateVar, p, par);}
    private HasAtom(Var var, Var predicateVar, IdPredicate p, ReasonerQuery par){
        super(var.has(predicateVar), predicateVar, p, par);
    }
    private HasAtom(TypeAtom a) { super(a);}

    @Override
    public Class<? extends VarProperty> getVarPropertyClass() { return HasAttributeTypeProperty.class;}

    @Override
    public Atomic copy(){
        return new HasAtom(this);
    }

    @Override
    public Set<TypeAtom> unify(Unifier u){
        Collection<Var> vars = u.get(getVarName());
        return vars.isEmpty()?
                Collections.singleton(this) :
                vars.stream().map(v -> new HasAtom(v, getPredicateVariable(), getTypePredicate(), this.getParentQuery())).collect(Collectors.toSet());
    }

    @Override
    public Atom rewriteWithTypeVariable() {
        return new HasAtom(getPattern(), getPredicateVariable().asUserDefined(), getTypePredicate(), getParentQuery());
    }

    @Override
    public Atom rewriteToUserDefined(Atom parentAtom) {
        return parentAtom.getPredicateVariable().isUserDefinedName()?
                new HasAtom(getPattern(), getPredicateVariable().asUserDefined(), getTypePredicate(), getParentQuery()) :
                this;
    }
}

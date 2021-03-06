/*
 * SonarQube
 * Copyright (C) 2009-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
import Backbone from 'backbone';
import React from 'react';
import SearchView from './search-view';

function contains (root, node) {
  while (node) {
    if (node === root) {
      return true;
    }
    node = node.parentNode;
  }
  return false;
}

export default React.createClass({
  getInitialState() {
    return { open: false };
  },

  componentDidMount() {
    key('s', () => {
      this.openSearch();
      return false;
    });
  },

  componentWillUnmount() {
    this.closeSearch();
    key.unbind('s');
  },

  openSearch() {
    window.addEventListener('click', this.onClickOutside);
    this.setState({ open: true }, this.renderSearchView);
  },

  closeSearch() {
    window.removeEventListener('click', this.onClickOutside);
    this.resetSearchView();
    this.setState({ open: false });
  },

  renderSearchView() {
    let searchContainer = this.refs.container;
    this.searchView = new SearchView({
      model: new Backbone.Model(this.props),
      hide: this.closeSearch
    });
    this.searchView.render().$el.appendTo(searchContainer);
  },

  resetSearchView() {
    if (this.searchView) {
      this.searchView.destroy();
    }
  },

  onClick(e) {
    e.preventDefault();
    if (this.state.open) {
      this.closeSearch();
    } else {
      this.openSearch();
    }
  },

  onClickOutside(e) {
    if (!contains(this.refs.dropdown, e.target)) {
      this.closeSearch();
    }
  },

  render() {
    const dropdownClassName = 'dropdown' + (this.state.open ? ' open' : '');
    return (
        <li ref="dropdown" className={dropdownClassName}>
          <a className="navbar-search-dropdown" href="#" onClick={this.onClick}>
            <i className="icon-search navbar-icon"/>&nbsp;<i className="icon-dropdown"/>
          </a>
          <div ref="container" className="dropdown-menu dropdown-menu-right"></div>
        </li>
    );
  }
});

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

export default Backbone.Model.extend({

  isDefault: function () {
    return this.get('isDefault');
  },

  url: function () {
    return baseUrl + '/api/qualitygates';
  },

  showUrl: function () {
    return this.url() + '/show';
  },

  deleteUrl: function () {
    return this.url() + '/destroy';
  },

  toggleDefaultUrl: function () {
    var method = this.isDefault() ? 'unset_default' : 'set_as_default';
    return this.url() + '/' + method;
  },

  sync: function (method, model, options) {
    var opts = options || {};
    opts.data = opts.data || {};
    opts.data.id = model.id;
    if (method === 'read') {
      opts.url = this.showUrl();
    }
    if (method === 'delete') {
      opts.url = this.deleteUrl();
      opts.type = 'POST';
    }
    return Backbone.ajax(opts);
  },

  toggleDefault: function () {
    var that = this;
    var opts = {
      type: 'POST',
      url: this.toggleDefaultUrl(),
      data: { id: this.id }
    };
    return Backbone.ajax(opts).done(function () {
      that.collection.toggleDefault(that);
    });
  }

});



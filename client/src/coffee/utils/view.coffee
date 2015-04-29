class View extends Backbone.View
  constructor: (parent) ->
    @_parent = parent
    @_parent = application.main_view if @name? and (not @_parent?)
    super

  initialize: () ->
    @el      = document.createElement 'div'
    @$el     = $(@el)

  render: () ->
    @$container().empty()
    @$container().append @$el
    application.current_view = @
    @

  $container: () ->
    if @name?
      $("[data-view='#{@name}']", @_parent.el)
    else
      $("body")

  $: (selector) ->
    $(selector, @$el)
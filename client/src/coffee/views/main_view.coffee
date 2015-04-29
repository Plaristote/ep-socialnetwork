class MainView extends View
  template: JST['main_view']

  initialize: () ->
    @session_view = new SessionView @

  render: () ->
    @$el.append @template()
    @session_view.render()
    super
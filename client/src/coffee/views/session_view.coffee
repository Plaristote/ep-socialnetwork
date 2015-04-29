class SessionView extends View
  name:     'session-view'
  template: JST['session']
  events:
    'click #connect-button':      'login'
    'click #disconnect-button':   'logout'
    'click #create-account':      'create_account'
    'click #userspace .username': 'show_account'

  initialize: () ->
    @listenTo application.session,      'login',  @loggedin.bind(@)
    @listenTo application.session,      'logout', @loggedout.bind(@)
    @listenTo application.current_user, 'change', @update_username.bind(@)
    super

  render: () ->
    @$el.html @template()
    @$('#userspace').hide()
    super

  create_account: () ->
    Backbone.history.navigate '/users/new', trigger: true

  login: () ->
    email    = @$("[name='email']").val()
    password = @$("[name='password']").val()
    application.session.connect email, password

  logout: () ->
    application.session.disconnect()
    
  loggedin: () ->
    @$('#connect-form').hide()
    @$('#userspace').show()

  loggedout: () ->
    @$('#connect-form').show()
    @$('#connect-form [name="password"]').val('')
    @$('#userspace').hide()

  show_account: () ->
    Backbone.history.navigate "/users/#{application.current_user.get 'id'}", trigger: true

  update_username: () ->
    first_name = application.current_user.get 'first_name'
    last_name  = application.current_user.get 'last_name'
    @$('#userspace .username').text "#{first_name} #{last_name}"
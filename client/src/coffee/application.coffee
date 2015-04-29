#= require_tree utils/
#= require_tree models/
#= require_tree collections/
#= require_tree views/
#= require_tree controllers/

class Application
  constructor: () ->
    console.info "Initializing Application"
    _.extend @, Backbone.Events

  initialize: () ->
    @users_controller = new UsersController
        
    @current_user = new User()

    @session   = new Session
    @session.on 'login',  @on_user_connected.bind(@)
    @session.on 'logout', @on_user_disconnected.bind(@)

    @main_view = new MainView
    @main_view.render()

  on_user_connected: (user_id) ->
    @current_user.set 'id', user_id
    @current_user.fetch()
    
  on_user_disconnected: (user_id) ->
    @current_user.set 'id', null
    @current_user.set 'friends_ids', null

$.ajaxSetup
  contentType: 'application/json'

$(document).ready ->
  Backbone.$ = jQuery
  window.application = new Application
  window.userview = new NewUserView
  application.initialize()
  
  Backbone.history.start pushState: false
  Backbone.history.navigate "/users", trigger: true
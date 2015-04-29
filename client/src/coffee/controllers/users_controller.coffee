class UsersController extends Controller
  routes:
    'users':        'index'
    'users/new':    'create'
    'users/update': 'update'
    'users/:id':    'show'

  index: () ->
    users = new UserCollection
    view  = new UsersView users
    view.render()
    users.fetch()

  show: (user_id) ->
    user = new User id: parseInt(user_id)
    view = new UserView user
    view.render()
    user.fetch()

  create: () ->
    view = new NewUserView()
    @listenTo view, 'subscribe', @perform_user_creation.bind(@)
    view.render()

  perform_user_creation: (view) ->
    user = new User()
    user.set view.get_attributes()
    user.save {},
      success: (data) => @on_user_created data.id
      error:   (data) -> view.on_subscribe_failure data

  on_user_created: (user_id) ->
    @navigate "/users/#{user_id}", trigger: true

  update: () ->
    view = new UpdateUserView()
    @listenTo view, 'update', @perform_user_update.bind(@)
    view.render()

  perform_user_update: (view) ->
    $.ajax
      method:   'PUT'
      url:      '/users'
      data:     JSON.stringify view.get_attributes()
      dataType: 'json'
      success:  (data) => @on_user_updated view
      error:    (data) -> view.on_update_failure data
      
  on_user_updated: (view) ->
    application.current_user.set view.get_attributes()

class Session extends Backbone.Model
  url: "/session"

  connect: (email, password) ->
    @set 'id',       null
    @set 'email',    email
    @set 'password', password
    @save {},
      error:          => @trigger 'login-failed'
      success: (data) => @trigger 'login', data.id

  disconnect: () ->
    @destroy
      error:   => @trigger 'logout-failed'
      success: => @trigger 'logout'
class UserCache extends Backbone.Collection
  model: User

  add: (user) ->
    existing_user = @find id: user.get('id')
    if existing_user?
      existing_user.set user.attributes
    else
      super user

  find_or_fetch: (attributes, callbacks) ->
    existing_user = @find attributes
    if existing_user?
      callbacks.success existing_user
    else
      user = new User id: attributes.id
      user.fetch
        success: -> callbacks.success user
        error:   -> callbacks.error()
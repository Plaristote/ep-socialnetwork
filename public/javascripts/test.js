var user_1 = {
  email: 'francis@gmail.com',
  first_name: "Francis",
  last_name: "Huster",
  password: "lepassword",
  location: "Paris",
  about: "Acteur"
};

var user_2 = {
  email: 'roger@rabbit.com',
  first_name: 'Roger',
  last_name: 'Rabbit',
  password: 'lepassword',
  location: 'Toonville',
  about: 'Toon'
};

function remove_account(callback) {
  $.ajax({
    method: 'DELETE',
    url: '/users',
    success: function() { console.log('Account successfully removed'); if (typeof callback != 'undefined') {callback();}},
    error: function() { console.log('Error: failed to remove account'); }
  });
}

function setup_account(user_data, callback) {
  $.ajax({
    method: 'POST',
    url: '/users',
    data: JSON.stringify(user_data),
    contentType: 'application/json',
    dataType: 'json',
    success: function(data) {
      user_data.id = data.id;
      callback(data);
    },
    error: function(data) {
      console.log('Error: cannot create account', data);
    }
  });
}

function connect_to_user(user_data, callback) {
  $.ajax({
    method: 'POST',
    url: '/session',
    data: JSON.stringify({ email: user_data.email, password: user_data.password }),
    contentType: 'application/json',
    dataType: 'json',
    success: function (data) {
      console.log('Connected to user', user_data);
      callback();
    },
    error: function (data) {
      console.log('Error: could not connect to user ' + user_data.email);
    }
  });
}

function disconnect(callback) {
  $.ajax({
    method: 'DELETE',
    url: '/session',
    success: function (data) {
      console.log('Successfully disconnected');
      callback();
    },
    error: function (data) {
      console.log('Error: Could not disconnect');
    }
  });
}

function send_message(user_data, message, callback) {
  $.ajax({
    method: 'POST',
    url: '/messages',
    data: JSON.stringify({ to: user_data.id, message: message }),
    contentType: 'application/json',
    dataType: 'json',
    success: function (data) {
      console.log("Message sent", user_data, message);
      callback(data);
    },
    error: function (data) {
      console.log("Error: failed to send message");
    }
  });
}

function show_messages(callback) {
  $.ajax({
    method: 'GET',
    url: '/messages',
    contentType: 'application/json',
    dataType: 'json',
    success: function (data) {
      callback(data.messages);
    },
    error: function (data) {
      console.log('Error: failed to fetch messages');
    }
  });
}

function add_friend(user_data, callback) {
  $.ajax({
    method: 'POST',
    url: '/friends',
    data: JSON.stringify({ friend: user_data.id }),
    contentType: 'application/json',
    dataType: 'json',
    success: function(data) {
      console.log('Friend added', user_data);
      callback(data);
    },
    error: function() { console.log("Error: failed to add friend");}
  });
}

function show_friends(callback) {
  $.ajax({
    method: 'GET',
    url: '/friends',
    success: callback,
    error: function() { console.log('Error: failed to fetch friend list'); }
  });
}

function delete_friend(user_data, callback) {
  $.ajax({
    method: 'DELETE',
    url: '/friends/user/' + user_data.id,
    success: function() {
      console.log('Friend deleted', user_data);
      callback();
    },
    error: function() { console.log('Error: could not delete friend'); }
  });
}

function add_post(user_data, options, callback) {
  $.ajax({
    method: 'POST',
    url: '/posts',
    data: JSON.stringify({ to: user_data.id, url: options.url, description: options.description }),
    contentType: 'application/json',
    dataType: 'json',
    success: function(data) {
      console.log('Successfully add post', options);
      callback(data);
    },
    error: function() { console.log('Error: couldnt add post'); }
  });
}

function show_posts(user_data, callback) {
  $.ajax({
    method: 'GET',
    url: '/posts/user/' + user_data.id,
    dataType: 'json',
    success: callback,
    error: function() { console.log('Error: couldnt get post index'); }
  });
}

function disable_post(post_id, callback) {
  $.ajax({
    method: 'PUT',
    url: '/posts/' + post_id,
    data: JSON.stringify({ enable: false }),
    contentType: 'application/json',
    dataType: 'json',
    success: function(data) {
      console.log('Successfully disabled post');
      callback(data);
    },
    error: function() { console.log('Could not update post'); }
  });
}

function add_picture(data, callback) {
  $.ajax({
    method: 'POST',
    url: '/pictures',
    data: JSON.stringify(data),
    contentType: 'application/json',
    dataType: 'json',
    success: function(data) {
      console.log('Successfully added picture');
      callback(data);
    },
    error: function() { console.log('Could not add picture');}
  });
}

function update_picture(picture_id, data, callback) {
  $.ajax({
    method: 'PUT',
    url: '/pictures/' + picture_id,
    data: JSON.stringify(data),
    contentType: 'application/json',
    dataType: 'json',
    success: function(data) {
      console.log('Successfully updated picture');
      callback(data);
    },
    error: function() { console.log('Could not update picture');}
  });
}

function show_pictures(user_data, callback) {
  $.ajax({
    method: 'GET',
    url: '/pictures/user/' + user_data.id,
    dataType: 'json',
    success: function(data) {
      callback(data);
    },
    error: function() { console.log('Could not fetch user pictures');}
  });
}

var test_socialnetwork = {
  session: {
    waiting_for_session: [],
    tasks_remaining: 0,
    connected: false,

    task_done: function() {
      if (test_socialnetwork.session.tasks_remaining > 0) {
        test_socialnetwork.session.tasks_remaining--;
        if (test_socialnetwork.session.tasks_remaining == 0)
          test_socialnetwork.session.disconnect();
      }
    },

    on_next_session: function(callback) {
      if (test_socialnetwork.session.connected == true)
        test_socialnetwork.session.waiting_for_session.push(callback);
      else {
        test_socialnetwork.session.connected = true;
        callback();
      }
    },

    disconnect: function() {
      disconnect(function() {
        if (test_socialnetwork.session.waiting_for_session.length > 0)
          test_socialnetwork.session.waiting_for_session.shift()();
        else
          test_socialnetwork.session.connected = false;
      })
    }
  },

  setup_accounts: function(callback) {
    setup_account(user_1, function() {
      setup_account(user_2, function() {
        callback();
      });
    });
  },

  remove_accounts: function(callback) {
    connect_to_user(user_1, function() {
      remove_account(function() {
        connect_to_user(user_2, function() {
          remove_account(callback);
        });
      });
    });
  }
};

function test_socialnetwork_rest_api() {
  test_socialnetwork.setup_accounts(function() {
    test_socialnetwork.session.on_next_session(function() {
      connect_to_user(user_1, function() {
        test_socialnetwork.session.tasks_remaining = 4;

        send_message(user_2, "Mon message", function() {
          test_socialnetwork.session.task_done();
        });

        add_friend(user_2, function() {
          show_friends(function(friends) {
            delete_friend(user_2, function() {
              show_friends(function(friends) {
                test_socialnetwork.session.task_done();
              });
            });
          });
        });

        add_post(user_2, { url: 'http://google.fr', description: 'Une description' }, function() {
          show_posts(user_2, function(data) {
            console.log('Received posts before disable', data);
            disable_post(data.posts[0].id, function() {
              show_posts(user_2, function(data) {
                console.log('Received posts after disable', data);
                test_socialnetwork.session.task_done();
              });
            });
          });
        });

        add_picture({ uri: '/assets/picture.jpg', description: 'la photo' }, function() {
          show_pictures(user_1, function(data) {
            console.log('Pictures before update', data);
            update_picture(data.pictures[0].id, { uri: '/assets/picture2.jpg' }, function() {
              show_pictures(user_1, function(data) {
                console.log('Pictures after update', data);
                test_socialnetwork.session.task_done();
              });
            });
          });
        });

      });
    });

    test_socialnetwork.session.on_next_session(function() {
      connect_to_user(user_2, function() {
        test_socialnetwork.session.tasks_remaining = 1;

        show_messages(function(messages) {
          test_socialnetwork.session.task_done();
        });
      });
    });

    test_socialnetwork.session.on_next_session(function() {
      test_socialnetwork.remove_accounts();
    });
  });
}
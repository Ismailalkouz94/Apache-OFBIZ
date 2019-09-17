'use strict';

(function($){

  $(function() {

    var datascource = {
      'id': '1',
      'name': 'Company',
      'title': 'Business Innovation',
      'children': [
        { 'id': '2', 'name': '', 'title': 'Marketing department',
          'children': [
            { 'id': '3', 'name': '', 'title': 'Program Manager [10002]',
              'children': [
                { 'id': '4', 'name': '', 'title': 'NULL' },
              ]
            }
          ]
        },
	    { 'id': '5', 'name': '', 'title': 'Accounting department' },
        { 'id': '6', 'name': '', 'title': 'Sales department',
          'children': [
            { 'id': '7', 'name': '', 'title': ' Business Analyst [10000]'}
          ]
        },
        { 'id': '8', 'name': '', 'title': ' Development Department',
          'children': [
            { 'id': '9', 'name': '', 'title': 'Development Team1',
              'children': [
					{ 'id': '10', 'name': '', 'title': 'Developer1'},
					{ 'id': '11', 'name': '', 'title': 'Developer2'},
					{ 'id': '12', 'name': '', 'title': 'Developer3'}
                          ]			
			},
            { 'id': '13', 'name': '', 'title': 'Development Team2'},            
			{ 'id': '14', 'name': '', 'title': 'Development Team3'},
			{ 'id': '15', 'name': '', 'title': 'Development Team4'},
          ]
        },
        { 'id': '16', 'name': '', 'title': ' Testing Department',
          'children': [
            { 'id': '17', 'name': '', 'title': ' TestingTeam1',
              'children': [
					{ 'id': '18', 'name': '', 'title': 'TestingTeamMember1'},
					{ 'id': '19', 'name': '', 'title': 'TestingTeamMember2'},
					{ 'id': '20', 'name': '', 'title': 'TestingTeamMember3'}
                          ]			
			},
            { 'id': '21', 'name': '', 'title': ' TestingTeam2'},            
          ]
        },
        { 'id': '22', 'name': '', 'title': 'Accountant Group',
          'children': [
            { 'id': '23', 'name': '', 'title': ' Accounting Administrator'},
            { 'id': '24', 'name': '', 'title': ' Business Analyst [10001]'},			
          ]
        },
        { 'id': '25', 'name': '', 'title': 'A Group of Lead Owners' },
        { 'id': '26', 'name': '', 'title': 'Programmer [DEMO100]',
          'children': [
            { 'id': '27', 'name': '', 'title': 'NULL'}			
          ]
        },
      ]
    };

    $('#chart-container').orgchart({
      'data' : datascource,
      'depth': 2,
      'nodeContent': 'title',
      'nodeID': 'id',
      'createNode': function($node, data) {
        var secondMenuIcon = $('<i>', {
          'class': 'fa fa-info-circle second-menu-icon',
          click: function() {
            $(this).siblings('.second-menu').toggle();
          }
        });
        var secondMenu = '<div class="second-menu"><img class="avatar" src="../img/avatar/' + data.id + '.jpg"></div>';
        $node.append(secondMenuIcon).append(secondMenu);
      }
    });

  });

})(jQuery);
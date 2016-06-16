INSERT INTO `selling`.`role` (`role_id`, `role_name`, `block_flag`, `create_time`) VALUES ('ROL00000001', 'admin', 0, '2016-5-3 11:00:00');
INSERT INTO `selling`.`role` (`role_id`, `role_name`, `block_flag`, `create_time`) VALUES ('ROL00000002', 'agent', 0, '2016-5-3 11:00:10');


INSERT INTO `selling`.`manager` (`manager_id`, `manager_username`, `manager_password`) VALUES ('MNG00000001', 'admin', '21232f297a57a5a743894a0e4a801fc3');

INSERT INTO `selling`.`agent` (`agent_id`, `upper_agent_id`, `agent_coffer`, `agent_refund`, `agent_name`, `agent_gender`, `agent_phone`, `agent_address`, `agent_password`, `agent_wechat`, `scale`, `agent_level`, `agent_granted`, `block_flag`, `create_time`) VALUES ('AGTvlorff50', NULL, 0, 0, '王旻', 'M', '18000000000', '江苏省南京市鼓楼区汉口路22号', 'e10adc3949ba59abbe56e057f20f883e', NULL, 0, 0, 1, 0, '2016-05-05 15:57:56');

INSERT INTO `selling`.`user` (`user_id`, `username`, `password`, `role_id`, `manager_id`, `agent_id`, `block_flag`, `create_time`) VALUES ('USR00000001', 'admin', '21232f297a57a5a743894a0e4a801fc3', 'ROL00000001', 'MNG00000001', NULL, 0, '2016-05-03 11:00:20');
INSERT INTO `selling`.`user` (`user_id`, `username`, `password`, `role_id`, `manager_id`, `agent_id`, `block_flag`, `create_time`) VALUES ('USRflowfr67', '18000000000', 'e10adc3949ba59abbe56e057f20f883e', 'ROL00000002', NULL, 'AGTvlorff50', 0, '2016-05-09 16:13:10');

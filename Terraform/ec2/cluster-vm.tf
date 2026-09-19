resource "aws_instance" "master" {
    ami = "ami-01a00762f46d584a1"
    instance_type = "c7i-flex.large"
    key_name = aws_key_pair.key.key_name
    subnet_id = var.private_sub_1
    vpc_security_group_ids = [var.k8s_master_sg]
    associate_public_ip_address = false
    tags = {
      Name = "K8s-Master"
    }
}

resource "aws_instance" "worker" {
    ami = "ami-01a00762f46d584a1"
    instance_type = "c7i-flex.large"
    key_name = aws_key_pair.key.key_name
    subnet_id = var.private_sub_2
    vpc_security_group_ids = [var.k8s_worker_sg]
    associate_public_ip_address = false
    tags = {
      Name = "K8s-Worker"
    }
}